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
 * SensorFusionActivity
 *
 * This activity demonstrates how sensor fusion can be used to improve accuracy
 * of rotation vector significantly.
 *
 * @see Rotating Objects Using Quaternions,
 *      https://www.gamasutra.com/view/feature/131686/rotating_objects_using_quaternions.php
 * @see Tutorial 17 : Rotations,
 *      www.opengl-tutorial.org/intermediate-tutorials/tutorial-17-quaternions/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SensorFusionActivity extends Activity implements SensorEventListener {

	private DecimalFormat df = new DecimalFormat("00.0");

	private SensorManager mSensorManager;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		String msg = "";
		switch (event.sensor.getType()) {
		case Sensor.TYPE_GAME_ROTATION_VECTOR:
			float[] angle = event.values;
			msg = "x-axis: " + df.format(angle[0] * 180) + " degrees\n";
			msg += "y-axis: " + df.format(angle[1] * 180) + " degrees\n";
			msg += "z-axis: " + df.format(angle[2] * 180) + "  degrees\n";
			break;
		default:
			return;
		}

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText(msg);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
		if (mLight != null) {
			mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
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