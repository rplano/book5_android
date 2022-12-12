package variationenzumthema_pr5;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BatteryActivity
 *
 * This activity shows how to use the battery as a temperature sensor.
 *
 * @see http://android-er.blogspot.de/2015/04/get-battery-information-using.html
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BatteryActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		String msg = "";
		msg += getBatteryLevel();
		msg += readBatteryState();

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText(msg);
	}

	private String getBatteryLevel() {
		String msg;
		BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
		if (Build.VERSION.SDK_INT >= 21) {
			int capacity = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
			msg = "Battery Level: " + capacity + "%\n";
		} else {
			msg = "Battery Level not supported!\n";
		}
		return msg;
	}

	private String readBatteryState() {
		StringBuilder sb = new StringBuilder();

		IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryIntent = registerReceiver(null, batteryIntentFilter);

		int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		sb.append("STATUS: " + getHumanReadableString("BATTERY_STATUS_", "unknown", status) + "\n");

		int plugged = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		sb.append("PLUGGED: " + getHumanReadableString("BATTERY_PLUGGED_", "not connected", plugged) + "\n");

		int health = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
		sb.append("HEALTH: " + getHumanReadableString("BATTERY_HEALTH_", "unknown", health) + "\n");

		int temperature = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
		float celsius = ((float) temperature) / 10;
		sb.append("TEMPERATURE: " + celsius + "°C\n");

		int voltage = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
		sb.append("VOLTAGE: " + voltage + "mV\n");

		Bundle extras = batteryIntent.getExtras();
		for (String key : extras.keySet()) {
			sb.append(key.toUpperCase() + ": ");
			sb.append(extras.get(key) + "\n");
		}

		return sb.toString();
	}

	private String getHumanReadableString(String prefix, String defalt, int value) {
		String result = defalt;
		try {
			Field[] flds = BatteryManager.class.getDeclaredFields();
			for (Field f : flds) {
				String name = f.getName();
				if (name.startsWith(prefix)) {
					if (f.getInt(null) == value) {
						result = name;
					}
				}
			}
		} catch (Exception e) {
			Log.e("BatteryActivity", e.toString());
		}
		return result;
	}

}
