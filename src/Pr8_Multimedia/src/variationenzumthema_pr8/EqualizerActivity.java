package variationenzumthema_pr8;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * EqualizerActivity
 *
 * This activity displays the microphones audio signal in a GraphView.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EqualizerActivity extends Activity implements Runnable {

	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	// private int bufferSize = 0;
	private boolean isRecording = true;

	private AudioRecord audioRecord;
	private GraphView2 gv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gv = new GraphView2(this);
		gv.setMin(Short.MIN_VALUE);
		gv.setMax(Short.MAX_VALUE);
		gv.setStyle(GraphView2.GraphStyle.LINE);
		gv.setColor(Color.RED);
		gv.setStrokeWidth(2);
		setContentView(gv);

		new Thread(this).start();
	}

	@Override
	protected void onPause() {
		isRecording = false;
		if (audioRecord != null) {
			audioRecord.stop();
		}
		super.onPause();
	}

	@Override
	public void run() {
		try {
			int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AUDIO_ENCODING);
			short[] data = new short[bufferSize];

			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AUDIO_ENCODING, bufferSize);
			audioRecord.startRecording();

			while (isRecording) {
				// record
				int length = audioRecord.read(data, 0, bufferSize);
				// add to queue
				// short[] copy = new short[bufferSize];
				// System.arraycopy(data, 0, copy, 0, copy.length);

				for (int j = 0; j < data.length; j++) {
					gv.addDataPoint(data[j]);
				}
				gv.postInvalidate();
			}

			audioRecord.stop();

		} catch (Exception e) {
			Log.i(getLocalClassName(), e.getMessage());
		}

	}
}
