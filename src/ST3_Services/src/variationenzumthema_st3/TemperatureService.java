package variationenzumthema_st3;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TemperatureService
 *
 * This service measures the temperature in regular intervals.
 *
 * @see Using the JobScheduler API on Android Lollipop,
 *      https://code.tutsplus.com/tutorials/using-the-jobscheduler-api-on-android-lollipop--cms-23562
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TemperatureService extends JobService {
	private final String TEMPERATURE_FILE_NAME = "temperatures.txt";

	@Override
	public boolean onStartJob(JobParameters params) {

		float celsius = getBatteryTemperature();
		Log.i("TemperatureService", "Temperature based on battery: " + celsius + "°C");

		appendTemperatureToFile(celsius);

		return true;
	}

	@Override
	public boolean onStopJob(JobParameters params) {
		Log.i("TemperatureService", "onStopJob() was called");
		return false;
	}

	private void appendTemperatureToFile(float celsius) {
		try {
			FileOutputStream fos = openFileOutput(TEMPERATURE_FILE_NAME, MODE_APPEND);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String dateTime = fmtOut.format(new Date());
			String msg = "" + dateTime + ": " + celsius + "°C \n";

			bw.write(msg);
			bw.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private float getBatteryTemperature() {
		IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryIntent = registerReceiver(null, batteryIntentFilter);

		int temperature = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
		float celsius = ((float) temperature) / 10;
		return celsius;
	}

}