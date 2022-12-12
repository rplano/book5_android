package variationenzumthema_ch5;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import variationenzumthema.ch5.R;

import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ListSensorsActivity
 *
 * This activity lists all available sensors on the device.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ListSensorsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		String msg = "";
		for (Sensor sensor : sensorList) {
			String type = "";
			if (Build.VERSION.SDK_INT >= 20) {
				type = sensor.getStringType();
			} else {
				type = "" + sensor.getType();
			}
			msg += sensor.getName() + "\n";
			msg += "   " + type + "\n";
			msg += "   " + sensor.getPower() + " mA\n";
			if (Build.VERSION.SDK_INT >= 21) {
				msg += "   " + sensor.isWakeUpSensor() + "\n";
			}
		}

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText(msg);
	}
}
