package variationenzumthema_pr8;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PianoTuningActivity
 *
 * This activity uses FFT for piano tuning. Or try to sing and see if you have
 * perfect pitch.
 * 
 * The highest detectable frequency is SAMPLE_RATE/2, the lowest is
 * SAMPLE_RATE/BUFFER_SIZE.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PianoTuningActivity extends Activity implements Runnable {

	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private final int BUFFER_SIZE = 2048; // must be power of 2
	private final double LOWEST_FREQUENCY = SAMPLE_RATE / (double) BUFFER_SIZE;

	private boolean isRecording = true;
	private AudioRecord audioRecord;
	private FFT fft;

	private GraphView2 gv;
	private TextView tvFrequency;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.piano_tuning_activity);

		TextView tvLowestFrequency = (TextView) findViewById(R.id.lowest);
		tvLowestFrequency.setText("" + (int) LOWEST_FREQUENCY + " Hz");
		TextView tvHighestFrequency = (TextView) findViewById(R.id.highest);
		tvHighestFrequency.setText("" + (SAMPLE_RATE / 2) + " Hz");

		gv = (GraphView2) findViewById(R.id.graphview);
		gv.setMin(Short.MIN_VALUE);
		gv.setMax(Short.MAX_VALUE);
		gv.setStyle(GraphView2.GraphStyle.LINE);
		gv.setColor(Color.RED);
		gv.setStrokeWidth(2);

		tvFrequency = (TextView) findViewById(R.id.frequency);

		new Thread(this).start();
	}

	@Override
	protected void onPause() {
		isRecording = false;
		if (audioRecord != null) {
			audioRecord.stop();
			audioRecord.release();
		}
		super.onPause();
	}

	@Override
	public void run() {
		try {
			int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			bufferSize = BUFFER_SIZE; // should be a power of 2

			fft = new FFT(bufferSize);
			short[] data = new short[bufferSize];

			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize);
			audioRecord.startRecording();

			while (isRecording) {
				audioRecord.read(data, 0, bufferSize);
				double[] magn = fft.doSimpleFFT(data);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						double pitch = fft.getMaxK() * LOWEST_FREQUENCY;
						String msg = "Detected: " + (int) pitch + " Hz";
						msg += " (" + getPitchName(getKey(pitch)) + ")";
						tvFrequency.setText(msg);
					}
				});

				gv.reset();
				gv.setMax(fft.getMax());
				gv.setMin(0);
				int len = Math.min(gv.getSize(), magn.length);
				for (int j = 0; j < len; j++) {
					gv.addDataPoint(magn[j]);
				}
				// move to the left
				int delta = gv.getSize() - len;
				for (int j = 0; j < delta - 10; j++) {
					gv.addDataPoint(0);
				}
				gv.postInvalidate();
			}

		} catch (Exception e) {
			Log.i(getLocalClassName(), e.getMessage());
		}

	}

	/**
	 * A4 (the reference pitch) is the 49th key from the left end of a piano
	 */
	private int getKey(double pitch) {
		double base = Math.pow(2.0, 1.0 / 12.0);
		double k = (Math.log(pitch) - Math.log(440.0)) / Math.log(base) + 49;
		if (k >= 0 && k <= 100) {
			return (int) Math.round(k);
		}
		return 0;
	}

	private String getPitchName(int key) {
		String[] pitchNames = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
		String pitchName = pitchNames[(key + 8) % 12];
		pitchName += ((key + 8) / 12);
		return pitchName;
	}
}
