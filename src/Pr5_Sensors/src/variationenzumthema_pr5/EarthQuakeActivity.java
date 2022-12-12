package variationenzumthema_pr5;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * EarthQuakeActivity
 *
 * This activity detects small accelerations, i.e., it is a toy-earthquake
 * detector.  
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EarthQuakeActivity extends Activity implements SensorEventListener {

	private final double EARTHQUAKE_THRESHOLD = 0.1;

	private SensorManager mSensorManager;
	private double[] accelLast = new double[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			double maxDelta = 0;
			for (int i = 0; i < 3; i++) {
				double accel = event.values[i];
				double delta = accelLast[i] - accel;
				if (delta > maxDelta) {
					maxDelta = delta;
				}
				accelLast[i] = accel;
			}

			if (maxDelta > EARTHQUAKE_THRESHOLD) {
				Toast.makeText(this, "Earthquake detected!", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			return;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		Sensor mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

}
