package variationenzumthema_pr5;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import variationenzumthema.pr5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * HauDenLukasActivity
 *
 * This activity detects large accelerations.
 * 
 * Disclaimer: By using this app you agree to not sue me in case your phone is
 * broken!
 * 
 * @see Hau den Lukas auf der Wiesn, https://www.youtube.com/watch?v=_AOdyo3tCwc
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HauDenLukasActivity extends Activity implements SensorEventListener {

	private final String[] ranks = { "Weltmeister", "Weibaheld", "Haderlump", "Anfänger", "G'schaftl Huaba",
			"Schlappschwanz" };
	private final int[] accelRequired = { 250, 200, 150, 100, 50, 0 };

	private SensorManager mSensorManager;
	private double oldAccel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		TextView tv = (TextView) findViewById(R.id.textview);

		double accel = Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1]
				+ event.values[2] * event.values[2]);

		if (accel > oldAccel) {
			oldAccel = accel;
			tv.setText("Acceleration: " + String.format("%1$,.1f", oldAccel) + " m/s^2\n");
			int i = 0;
			for (; i < ranks.length; i++) {
				if (oldAccel > accelRequired[i])
					break;
			}
			tv.append(ranks[i]);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
}
