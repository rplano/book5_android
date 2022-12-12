package variationenzumthema_pr8;

import acm_graphics.*;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * LoudnessActivity
 * 
 * Animate the size of a GOval based on loudness.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class LoudnessActivity extends GraphicsProgram {

	// constants
	private final int BALL_MIN_SIZE = 10;
	private final int DELAY = 40;

	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private final int MAX_LOUDNESS = Short.MAX_VALUE;

	// instance variables
	private GOval ball;
	private boolean isRecording = true;
	private AudioRecord audioRecord;

	public void run() {
		waitForTouch();
		setupUI();

		try {
			int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			short[] data = new short[bufferSize];

			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize);
			audioRecord.startRecording();

			while (isRecording) {

				int length = audioRecord.read(data, 0, bufferSize);

//				short[] copy = new short[bufferSize];
//				System.arraycopy(data, 0, copy, 0, copy.length);

				int loudness = calculateLoudness(data);

				changeBallSize(loudness);

				pause(DELAY);
			}

		} catch (Exception e) {
			Log.i(getLocalClassName(), e.getMessage());
		}
	}

	private int calculateLoudness(short[] copy) {
		double average = 0;
		for (int i = 0; i < copy.length; i++) {
			average += Math.abs(copy[i]);
		}
		average /= copy.length;
		return (int) average;
	}

	private void changeBallSize(int loudness) {
		int size = loudness * getWidth() / MAX_LOUDNESS + BALL_MIN_SIZE;

		int x = (getWidth() - size) / 2;
		int y = (getHeight() - size) / 2;
		ball.setBounds(x, y, size, size);
	}

	private void setupUI() {
		setBackground(Color.WHITE);

		ball = new GOval(BALL_MIN_SIZE, BALL_MIN_SIZE);
		ball.setFillColor(Color.GREEN);
		ball.setFilled(true);
		add(ball, getWidth() / 2, getHeight() / 2);
	}

	@Override
	protected void onPause() {
		isRecording = false;
		if (audioRecord != null) {
			audioRecord.stop();
		}
		super.onPause();
	}
}
