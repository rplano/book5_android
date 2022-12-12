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
 * MotionActivity
 *
 * This activity shows how to use the raw motion sensors, that is accelerometer,
 * gyroscope and magnetometer.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MotionActivity extends Activity implements SensorEventListener {

	private DecimalFormat df = new DecimalFormat("00.00");
	private String[] msg = new String[3];

	private SensorManager mSensorManager;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float[] acceleration_in_m_per_s2 = event.values;
			msg[0] = "ACCEL:  " + df.format(acceleration_in_m_per_s2[0]) + ", " + df.format(acceleration_in_m_per_s2[1])
					+ ", " + df.format(acceleration_in_m_per_s2[2]) + " m/s2\n";
			break;
		case Sensor.TYPE_GYROSCOPE:
			float[] rotation_in_rad_per_s = event.values;
			msg[1] = "GYRO:   " + df.format(rotation_in_rad_per_s[0]) + ", " + df.format(rotation_in_rad_per_s[1])
					+ ", " + df.format(rotation_in_rad_per_s[2]) + " rad/s\n";
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			float[] field_strength_in_micro_tesla = event.values;
			msg[2] = "MAGNET: " + df.format(field_strength_in_micro_tesla[0]) + ", "
					+ df.format(field_strength_in_micro_tesla[1]) + ", " + df.format(field_strength_in_micro_tesla[2])
					+ " microTesla\n";
			break;
		default:
			return;
		}

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText(msg[0] + msg[1] + msg[2]);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (mAccelerometer != null) {
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		}

		Sensor mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		if (mGyroscope != null) {
			mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		}

		Sensor mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		if (mMagneticField != null) {
			mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
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