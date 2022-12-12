package variationenzumthema_ch5;

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
 * EnvironmentalActivity
 *
 * This activity shows how to use the light, pressure, temperature and proximity
 * sensors, if they exist. Note that the proximity sensor only triggers when you
 * hold the hand or some other object in front of it.
 *
 * @see https://developer.android.com/guide/topics/sensors/sensors_environment.html#sensors-using-temp
 * @see https://developer.android.com/guide/topics/sensors/sensors_position.html#sensors-pos-prox
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EnvironmentalActivity extends Activity implements SensorEventListener {

	private final int DELAY = 1000000; // once a second
	//private final int DELAY = SensorManager.SENSOR_DELAY_NORMAL;
	private SensorManager mSensorManager;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		TextView tv = (TextView) findViewById(R.id.textview);
		String msg = "";
		switch (event.sensor.getType()) {
		case Sensor.TYPE_LIGHT:
			float illuminance_in_lx = event.values[0];
			msg += "TYPE_LIGHT: " + illuminance_in_lx + " lx\n";
			break;
		case Sensor.TYPE_PRESSURE:
			float pressure_in_millibars = event.values[0];
			msg += "TYPE_PRESSURE: " + pressure_in_millibars + " mbar\n";
			break;
		case Sensor.TYPE_RELATIVE_HUMIDITY:
			float humidity_in_percent = event.values[0];
			msg += "TYPE_RELATIVE_HUMIDITY: " + humidity_in_percent + " %\n";
			break;
		case Sensor.TYPE_TEMPERATURE:
			float temperature_in_celsius = event.values[0];
			msg += "TYPE_TEMPERATURE: " + temperature_in_celsius + " °C\n";
			break;
		case Sensor.TYPE_PROXIMITY:
			float distance_in_cm = event.values[0];
			msg += "TYPE_PROXIMITY: " + distance_in_cm + " cm\n";
			break;
		default:
			return;
		}

		msg += tv.getText().toString(); 
		tv.setText(msg);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		if (mLight != null) {
			mSensorManager.registerListener(this, mLight, DELAY);
		}

		Sensor mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		if (mPressure != null) {
			//mSensorManager.registerListener(this, mPressure, DELAY);
		}

		Sensor mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
		if (mHumidity != null) {
			mSensorManager.registerListener(this, mHumidity, DELAY);
		}

		Sensor mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
		if (mTemperature != null) {
			mSensorManager.registerListener(this, mTemperature, DELAY);
		}

		Sensor mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		if (mProximity != null) {
			mSensorManager.registerListener(this, mProximity, DELAY);
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