package variationenzumthema_pr8;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.AudioTrack.OnPlaybackPositionUpdateListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MorseActivity
 *
 * This activity enables communication between two phones with sound using morse
 * code.
 *
 * Infrared remote controls also work with similar idea.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MorseActivity extends Activity {
	private final int NR_OF_WAVES = 10;
	private final double FREQUENCY = 8800;
	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000

	// from Wikipedia.de:
	private final String[] MORSE_ALPHABET = { "01", "1000", "1010", "100", "0", "0010", "110", "0000", "00", "0111",
			"101", "0100", "11", "10", "111", "0110", "1101", "010", "000", "1", "001", "0001", "011", "1001", "1011",
			"1100" };
	private Map<String, Character> morseLookupTable;

	private boolean isRecording = true;
	private AudioTrack audioTrack;
	private TextView tv;
	private GraphView2 gv;
	private int unitLen = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morse_activity);

		final short[] tmp = generateSound();
		unitLen = tmp.length;
		Log.i("MorseActivity", "unitLen=" + unitLen);

		morseLookupTable = new HashMap<String, Character>();
		for (int i = 0; i < MORSE_ALPHABET.length; i++) {
			char c = (char) (i + 'A');
			morseLookupTable.put(MORSE_ALPHABET[i], c);
		}

		final EditText et = (EditText) findViewById(R.id.editText);

		tv = (TextView) findViewById(R.id.textview);

		Button btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MorseStateMachine msm = new MorseStateMachine();
				String morse = msm.convertStringToMorseCode(et.getText().toString());

				int bufferSize = morse.length() * unitLen;
				short[] data = new short[bufferSize];

				for (int i = 0; i < morse.length(); i++) {
					char c = morse.charAt(i);
					if (c == '=') {
						System.arraycopy(tmp, 0, data, i * unitLen, unitLen);
					} else if (c == '_') {
						// do nothing
					} else {
						Log.e("MorseActivity", "we should never get here!");
					}
				}

				playSound(data);
			}
		});

		gv = (GraphView2) findViewById(R.id.graphview);
		gv.setMin(Short.MIN_VALUE);
		gv.setMax(Short.MAX_VALUE);
		gv.setStyle(GraphView2.GraphStyle.LINE);
		gv.setColor(Color.RED);
		gv.setStrokeWidth(2);

		Recorder recorder = new Recorder();
		new Thread(recorder).start();
	}

	private void playSound(short[] data) {
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 2 * data.length, AudioTrack.MODE_STATIC);
		audioTrack.setNotificationMarkerPosition(2 * data.length);
		audioTrack.setPlaybackPositionUpdateListener(new OnPlaybackPositionUpdateListener() {
			@Override
			public void onPeriodicNotification(AudioTrack track) {
				// nothing to do
			}

			@Override
			public void onMarkerReached(AudioTrack track) {
				Log.d("MorseActivity", "Audio track end reached...");
				// isRecording = true;
			}
		});
		audioTrack.flush();
		audioTrack.write(data, 0, data.length);
		audioTrack.play();
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	@Override
	protected void onPause() {
		isRecording = false;
		super.onPause();
	}

	private short[] generateSound() {
		double time = NR_OF_WAVES / FREQUENCY;
		int bufferSize = (int) (time * SAMPLE_RATE) + 1;
		short[] data = new short[bufferSize];
		double factor = 2 * Math.PI / (SAMPLE_RATE / FREQUENCY);
		for (int i = 0; i < data.length; i++) {
			data[i] = (short) (Short.MAX_VALUE * 0.95 * functionSquare(factor, i));
		}
		return data;
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

	class Recorder implements Runnable {
		// private int bufferSize = 0;
		private short[] data;
		private MorseStateMachine msm;

		public Recorder() {
			data = generateSound();
			msm = new MorseStateMachine();
		}

		@Override
		public void run() {
			try {
				int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				Log.i("Recorder", "bufferSize=" + bufferSize);
				short[] data = new short[bufferSize];
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
						AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
				audioRecord.startRecording();

				// record
				while (isRecording) {
					int length = audioRecord.read(data, 0, bufferSize);
					short[] copy = new short[bufferSize];
					System.arraycopy(data, 0, copy, 0, copy.length);
					evaluateDistances(copy);
				}

				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;

			} catch (Exception e) {
				Log.i(getLocalClassName(), e.getMessage());
			}

		}

		private void evaluateDistances(short[] recordingData) {
			// short[] highPassArray = highPassFilter(recordingData, 10);
			double[] highPassArray = filterLowFrequencies(recordingData);

			double[] average = filterAverage(highPassArray);

			double[] quantize = filterQuantize(average);

			// gv.setMin(min);
			// gv.setMax(max);
			gv.setMin(-100);
			gv.setMax(1200);
			for (int i = 0; i < gv.getSize(); i++) {
				gv.addDataPoint(quantize[i]);
			}
			gv.postInvalidate();

			translate(quantize);
		}

		int slCounter0 = -1; // signal length counter
		int slCounter1 = -1; // signal length counter

		private void translate(double[] quantize) {
			for (int i = 0; i < quantize.length; i++) {
				// do we have a signal?
				if (quantize[i] == 1000) {
					// we have a '1' signal
					if (slCounter1 > -1) {
						// last signal also was a '1'
						slCounter1++;

					} else {
						// last signal was a '0'
						if (slCounter0 > unitLen / 2) {
							Log.i("MorseActivity", "slCounter0=" + slCounter0);
							addToCode(slCounter0, false);
						}
						slCounter0 = -1;
						slCounter1 = 0;
					}

				} else {
					// we have a '0' signal
					if (slCounter0 > -1) {
						// last signal also was a '0'
						slCounter0++;

					} else {
						// last signal was a '1'
						if (slCounter1 > unitLen / 2) {
							Log.i("MorseActivity", "slCounter1=" + slCounter1);
							addToCode(slCounter1, true);
						}
						slCounter1 = -1;
						slCounter0 = 0;

					}
				}
			}
			if (code.length() > 0) {
				Log.i("MorseActivity", "code=" + code);
				msm.addNewString(code);
				final String soFar = msm.getMorseMessage();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv.append(soFar);
					}
				});
			}
			code = "";
		}

		private String code = "";

		private void addToCode(double time, boolean on) {
			int units = (int) Math.round(time / unitLen);
			if (on) {
				code += createNChars("=", units);
			} else {
				code += createNChars("_", units);
			}
		}

		private String createNChars(String ch, int units) {
			String ret = "";
			if (units < 10) {
				for (int i = 0; i < units; i++) {
					ret += ch;
				}
			} else {
				ret += "X" + ch + "X";
			}
			return ret;
		}

		private double[] filterQuantize(double[] average) {
			double[] quantize = new double[average.length];
			for (int i = 0; i < average.length; i++) {
				if (average[i] > max / 2) {
					quantize[i] = 1000;
				}
			}
			return quantize;
		}

		double min, max;

		private double[] filterAverage(double[] highPassArray) {
			min = Double.MAX_VALUE;
			max = 100;
			// length of bit = NR_OF_WAVES * SAMPLE_RATE / FREQUENCY = 30
			int AVG_FACTOR = 2 * unitLen / NR_OF_WAVES;
			double[] average = new double[highPassArray.length];
			double avg = 0;
			for (int i = 0; i < average.length; i++) {
				avg = (avg * (AVG_FACTOR - 1) + Math.abs(highPassArray[i])) / AVG_FACTOR;
				average[i] = avg;
				min = Math.min(min, avg);
				max = Math.max(max, avg);
			}
			return average;
		}

		private final float LOW_PASS_FACTOR = 0.3f;

		private double[] filterLowFrequencies(short[] recordingData) {
			double[] highPassArray = new double[recordingData.length];
			double avg = 0;
			for (int i = 0; i < recordingData.length; i++) {
				avg = lowPass(recordingData[i], avg);
				highPassArray[i] = highPass(recordingData[i], avg);
			}
			return highPassArray;
		}

		private double lowPass(double current, double average) {
			return average * LOW_PASS_FACTOR + current * (1 - LOW_PASS_FACTOR);
		}

		private double highPass(double current, double average) {
			return current - average;
		}
	}
}
