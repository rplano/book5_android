package variationenzumthema_st3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * StepCounterService
 *
 * This service listens to the step counter sensor to count the users steps.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class StepCounterService extends Service implements SensorEventListener {

	private SensorManager mSensorManager;

	@Override
	public void onCreate() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "StepCounterService starting", Toast.LENGTH_SHORT).show();

		Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

		// if we get killed, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		mSensorManager.unregisterListener(this);
		super.onDestroy();
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		float steps = event.values[0];
		Toast.makeText(this, "steps=" + steps, Toast.LENGTH_SHORT).show();
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
