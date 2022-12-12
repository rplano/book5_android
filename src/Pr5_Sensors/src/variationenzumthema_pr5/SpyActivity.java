package variationenzumthema_pr5;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.pr5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SpyActivity
 *
 * This activity uses the accelerometer as microphone.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SpyActivity extends Activity implements SensorEventListener {

	private final float LOW_PASS_FACTOR = 0.8f;
	// private final int TIME_RESOLUTION = 100;

	private final int SCALE = 80; // should be a gcd of FREQUENCY
	private final int FREQUENCY = 8000;
	private final int TIME = 2; // seconds

	private TextView mTextView1;
	private Button btnAccel;
	private Button btnAgain;

	private SensorManager mSensorManager;
	private float[] oldValues = new float[3];

	private int rawPointer = TIME * FREQUENCY / SCALE;
	private float[] rawData = new float[TIME * FREQUENCY / SCALE];
	private float min, max;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spy_activity);

		mTextView1 = (TextView) findViewById(R.id.text1);

		btnAccel = (Button) findViewById(R.id.btnAccel);
		btnAccel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rawPointer = 0;
				btnAccel.setEnabled(false);
			}
		});

		btnAgain = (Button) findViewById(R.id.btnAgain);
		btnAgain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMinMax();
				short[] buffer = convertToAudio();
				playAudio(buffer);
			}
		});

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	private void getMinMax() {
		min = Float.MAX_VALUE;
		max = Float.MIN_VALUE;
		for (int i = 0; i < rawData.length; i++) {
			float value = rawData[i];
			if (value > max) {
				max = value;
			}
			if (value < min) {
				min = value;
			}
		}
	}

	private short[] convertToAudio() {
		short[] buffer = new short[TIME * FREQUENCY];
		float maxMinusMin = max - min;
		for (int i = 0; i < TIME * FREQUENCY; i++) {
			// scale between 0 and 1:
			float v = (rawData[i / SCALE] - min) / maxMinusMin;
			// scale to max range of short:
			short vv = (short) (2 * Short.MAX_VALUE * (v - 0.5));
			buffer[i] = vv;
		}
		return buffer;
	}

	private void playAudio(short[] buffer) {
		int audioLength = buffer.length;
		AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, FREQUENCY,
				AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, audioLength,
				AudioTrack.MODE_STREAM);
		audioTrack.play();
		audioTrack.write(buffer, 0, audioLength);
	}

	public void onSensorChanged(SensorEvent event) {
		if (rawPointer < TIME * FREQUENCY / SCALE) {
			float[] values = event.values;
			float delta = 0;
			for (int i = 0; i < 3; i++) {
				delta += values[i] - oldValues[i];
				oldValues[i] = values[i];
			}
			// we do not want the first value:
			if (delta < 9) {
				rawData[rawPointer] = delta;
				rawPointer++;
			}

		} else {
			getMinMax();
			mTextView1.setText("size: " + rawData.length + ", min=" + min + " ,max=" + max);
			btnAccel.setEnabled(true);
		}
	}

	private float[] background = new float[3];

	public void onSensorChanged2(SensorEvent event) {
		if (rawPointer < TIME * FREQUENCY / SCALE) {
			float[] values = event.values;

			// low-pass
			for (int i = 0; i < 3; i++) {
				background[i] = lowPass(values[i], background[i]);
			}

			// high-pass filter
			float[] highPass = new float[3];
			for (int i = 0; i < 3; i++) {
				highPass[i] = highPass(values[i], background[i]);
			}

			float delta = 0;
			for (int i = 0; i < 3; i++) {
				delta += Math.abs(background[i]);
			}
			// we do not want the first value:
			if (rawPointer >= 0) {
				rawData[rawPointer] = delta;
			}
			rawPointer++;

		} else {
			getMinMax();
			mTextView1.setText("size: " + rawData.length + ", min=" + min + " ,max=" + max);
			btnAccel.setEnabled(true);
		}
	}

	// passes signals below a certain cutoff frequency
	private float lowPass(float current, float average) {
		return average * LOW_PASS_FACTOR + current * (1 - LOW_PASS_FACTOR);
	}

	// passes signals above a certain cutoff frequency
	private float highPass(float current, float average) {
		return current - average;
	}

	@Override
	protected void onResume() {
		super.onResume();

		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);

		// mSensorManager.registerListener(this,
		// mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
		// SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
