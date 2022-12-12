package variationenzumthema_pr8;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SonarActivity
 *
 * This activity uses the echo of a signal to estimate distances.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SonarActivity extends Activity {

	private final int NR_OF_WAVES = 4;
	private final double FREQUENCY = 8800;
	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000
	private int waveForm = 1; // 0 = sin, 1 = square, 2 = sawtooth, 3 =
								// triangle, 4 = staircase

	private AudioTrack audioTrack;
	private short[] data;
	private GraphView2 gv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sonar_activity);

		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start recording thread
				// buffer for a tenth of a second = 30 meters
				int bufferSize = SAMPLE_RATE / 10;
				// isRecording = true;
				Recorder recorder = new Recorder(bufferSize);
				new Thread(recorder).start();

				// play sound
				playSound();

				// wait half a second
				// pause(500);

			}
		});

		gv = (GraphView2) findViewById(R.id.graphview);
		gv.setMin(Short.MIN_VALUE);
		gv.setMax(Short.MAX_VALUE);
		gv.setStyle(GraphView2.GraphStyle.LINE);
		gv.setColor(Color.RED);
		gv.setStrokeWidth(2);
	}

	private void playSound() {
		double time = NR_OF_WAVES / FREQUENCY;
		int bufferSize = (int) (time * SAMPLE_RATE) + 1;
		generateSound(bufferSize);

		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 2 * data.length, AudioTrack.MODE_STATIC);
		audioTrack.flush();
		audioTrack.write(data, 0, data.length);
		audioTrack.play();
	}

	@Override
	protected void onPause() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
		}
		super.onPause();
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}

	}

	private void generateSound(int bufferSize) {
		data = new short[bufferSize];
		double factor = 2 * Math.PI / (SAMPLE_RATE / FREQUENCY);
		for (int i = 0; i < data.length; i++) {
			data[i] = (short) (Short.MAX_VALUE * 0.95 * function(factor, i));
		}
	}

	private double function(double factor, int i) {
		switch (waveForm) {
		case 1:
			return functionSquare(factor, i);
		case 2:
			return functionSawTooth(factor, i);
		case 3:
			return functionTriangle(factor, i);
		case 4:
			return functionStaircase(factor, i);
		default:
			return functionSin(factor, i);
		}
	}

	/**
	 * Use any periodic function with a period of 2*PI
	 * 
	 * @return values between -1 and +1
	 */
	private double functionSin(double factor, int i) {
		return Math.sin(i * factor);
	}

	private double functionSquare(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI);
		if (x < Math.PI) {
			return -1;
		} else {
			return +1;
		}
	}

	private double functionSawTooth(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI);
		return x / (Math.PI) - 1;
	}

	private double functionTriangle(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI);
		if (x < Math.PI) {
			return 2 * x / (Math.PI) - 1;
		} else {
			return 2 * (2 * Math.PI - x) / (Math.PI) - 1;
		}
	}

	private double functionStaircase(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI); // 0..2*PI
		x = x / (2 * Math.PI); // 0..1
		x = x * 10; // 0..9.999
		int s = (int) x; // 0..9
		s = s - 5; // -5..4
		return s / 5.0;
	}

	class Recorder implements Runnable {
		private int bufferSize = 0;

		public Recorder(int bufferSize) {
			this.bufferSize = bufferSize;
		}

		@Override
		public void run() {
			try {
				Log.i("Recorder", "bufferSize=" + bufferSize);
				short[] data = new short[bufferSize];
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
						AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
				audioRecord.startRecording();

				// record
				int length = audioRecord.read(data, 0, bufferSize);
				short[] copy = new short[bufferSize];
				System.arraycopy(data, 0, copy, 0, copy.length);

				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;

				evaluateDistances(copy);

			} catch (Exception e) {
				Log.i(getLocalClassName(), e.getMessage());
			}

		}

		private void evaluateDistances(short[] recordingData) {
			// Log.i("Recorder", "data=" + data.length);

			// short[] highPassArray = highPassFilter(recordingData, 10);

			// double[] crossCorrelationArray = crossCorrelation(highPassArray);
			double[] crossCorrelationArray = crossCorrelation(recordingData);
			gv.setMin(min);
			gv.setMax(max);
			// gv.setMin(-1000);
			// gv.setMax(5000);
			for (int i = maxK; i < gv.getSize() + maxK; i++) {
				gv.addDataPoint(crossCorrelationArray[i]);
				// gv.addDataPoint(recordingData[i]);
				// gv.addDataPoint(highPassArray[i]);
			}
			gv.postInvalidate();
		}

		private short[] highPassFilter(short[] recordingData, int length) {
			short[] highPassArray = new short[recordingData.length];
			short average = recordingData[0];
			for (int i = 1; i < highPassArray.length; i++) {
				average = (short) (((length - 1) * average + recordingData[i]) / length);
				highPassArray[i] = (short) (recordingData[i] - average);
			}
			return highPassArray;
		}

		private double min, max;
		private int maxK = 0;

		/**
		 * @see https://en.wikipedia.org/wiki/Cross-correlation
		 * @param recordingData
		 * @return
		 */
		private double[] crossCorrelation(short[] recordingData) {
			min = Double.MAX_VALUE;
			max = Double.MIN_VALUE;
			double[] crossCorrelationArray = new double[recordingData.length];
			for (int i = 0; i < recordingData.length - data.length; i++) {
				double tmpi = 0;
				for (int j = 0; j < data.length; j++) {
					tmpi += Math.abs(data[j] * recordingData[i + j]);
					// tmpi += data[j] * recordingData[i + j];
				}
				crossCorrelationArray[i] = tmpi;
				min = Math.min(min, crossCorrelationArray[i]);
				if (max < crossCorrelationArray[i]) {
					max = crossCorrelationArray[i];
					maxK = i;
				}
			}

			Log.i("crossCorrelation", "crossCorrelationArray.length" + crossCorrelationArray.length);
			Log.i("crossCorrelation", "data.length" + data.length);
			Log.i("crossCorrelation", "min=" + min);
			Log.i("crossCorrelation", "max=" + max);
			return crossCorrelationArray;
		}
	}
}
