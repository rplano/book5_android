package variationenzumthema_ch5;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import variationenzumthema.ch5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * FilterActivity
 *
 * This activity shows how to use low-pass and high-pass filters to filter the
 * raw motion sensors data.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FilterActivity extends Activity implements SensorEventListener {

	private final float LOW_PASS_FACTOR = 0.8f;
	private final int TIME_RESOLUTION = 100;

	private DecimalFormat df = new DecimalFormat("00.00");
	private String[] msg = new String[3];

	private SensorManager mSensorManager;

	private long lastTime;
	private float[] avg_acceleration = new float[3];

	@Override
	public final void onCreate(Bundle savedInstanceState) {
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
			case Sensor.TYPE_ACCELEROMETER:
				float[] acceleration_in_m_per_s2 = event.values;

				// low-pass
				for (int i = 0; i < 3; i++) {
					avg_acceleration[i] = lowPass(acceleration_in_m_per_s2[i], avg_acceleration[i]);
				}

				// high-pass
				float[] high_pass = new float[3];
				for (int i = 0; i < 3; i++) {
					high_pass[i] = highPass(acceleration_in_m_per_s2[i], avg_acceleration[i]);
				}

				msg[0] = "no filter:  " + df.format(acceleration_in_m_per_s2[0]) + ", "
						+ df.format(acceleration_in_m_per_s2[1]) + ", " + df.format(acceleration_in_m_per_s2[2]) + "\n";
				msg[0] += "low-pass:   " + df.format(avg_acceleration[0]) + ", " + df.format(avg_acceleration[1]) + ", "
						+ df.format(avg_acceleration[2]) + "\n";
				msg[0] += "high-pass:  " + df.format(high_pass[0]) + ", " + df.format(high_pass[1]) + ", "
						+ df.format(high_pass[2]) + "\n";
				break;
			default:
				return;
			}

			TextView tv = (TextView) findViewById(R.id.textview);
			tv.setText(msg[0]);
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

		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (mAccelerometer != null) {
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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