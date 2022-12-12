package variationenzumthema_pr5;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MetalDetectorActivity
 *
 * This activity implements a simple metal detector. It actually detects
 * magnetic fields, and not metal.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MetalDetectorActivity extends Activity implements SensorEventListener {

	private final float LOW_PASS_FACTOR = 0.8f;
	private final int TIME_RESOLUTION = 100;

	private DecimalFormat df = new DecimalFormat("0.");
	private String msg = "";

	private SensorManager mSensorManager;
	private long lastTime;
	private float[] magneticBackground = new float[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTime > TIME_RESOLUTION) {
			lastTime = currentTime;

			switch (event.sensor.getType()) {
			case Sensor.TYPE_MAGNETIC_FIELD:
				float[] field_strength_in_micro_tesla = event.values;

				// low-pass
				for (int i = 0; i < 3; i++) {
					magneticBackground[i] = lowPass(field_strength_in_micro_tesla[i], magneticBackground[i]);
				}

				// high-pass filter
				float[] highPass = new float[3];
				for (int i = 0; i < 3; i++) {
					highPass[i] = highPass(field_strength_in_micro_tesla[i], magneticBackground[i]);
				}

				msg = "x: " + df.format(highPass[0]) + "\n";
				msg += "y: " + df.format(highPass[1]) + "\n";
				msg += "z: " + df.format(highPass[2]) + "\n";
				break;
			}

			TextView tv = (TextView) findViewById(R.id.textview);
			tv.setText(msg);
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

		Sensor mMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		if (mMagnet != null) {
			mSensorManager.registerListener(this, mMagnet, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			Toast.makeText(this, "Your device has no magnetic sensors!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}