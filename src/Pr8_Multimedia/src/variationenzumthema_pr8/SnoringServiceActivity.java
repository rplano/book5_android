package variationenzumthema_pr8;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SnoringServiceActivity
 *
 * This activity listens for loud sounds and starts an alarm if detected.
 *
 * Should run as a service...
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SnoringServiceActivity extends Activity implements Runnable {

	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private final double LOUDNESS_THRESHOLD = Short.MAX_VALUE / 2;
	private final int BUFFER_SIZE = SAMPLE_RATE / 2;

	private boolean isRecording = true;
	private AudioRecord audioRecord;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Thread(this).start();
	}

	@Override
	protected void onPause() {
		isRecording = false;
		if (audioRecord != null) {
			if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
				audioRecord.stop();
			}
			audioRecord.release();
		}
		super.onPause();
	}

	@Override
	public void run() {
		try {
			// int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
			// AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
			short[] data = new short[BUFFER_SIZE];

			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
			audioRecord.startRecording();

			while (isRecording) {

				int length = audioRecord.read(data, 0, BUFFER_SIZE);

				short[] copy = new short[BUFFER_SIZE];
				System.arraycopy(data, 0, copy, 0, copy.length);

				int averageLoudness = calculateLoudness(copy);
//				double averageLoudness = 0;
//				for (int i = 0; i < copy.length; i++) {
//					averageLoudness += Math.abs(copy[i]);
//				}
//				averageLoudness /= copy.length;
				Log.i("SnoringServiceActivity", "averageLoudness=" + averageLoudness);

				if (averageLoudness > LOUDNESS_THRESHOLD) {
					isRecording = false;
					audioRecord.stop();

					// start alarm
					Intent intent = new Intent(this, SnoringAlarmActivity.class);
					startActivity(intent);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
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
}