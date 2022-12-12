package variationenzumthema_st3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * StepCounterBinderService
 *
 * This service listens to the step counter sensor to count the users steps.
 * Through a Binder it allows an activity to get access to the steps variable.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class StepCounterBinderService extends Service implements SensorEventListener {
	private final IBinder mBinder = new StepBinder();

	private SensorManager mSensorManager;
	private float steps = -1;

	public class StepBinder extends Binder {
		StepCounterBinderService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return StepCounterBinderService.this;
		}
	}

	public float getNumberOfSteps() {
		return steps;
	}

	@Override
	public void onCreate() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		steps = event.values[0];
		// Do something with this sensor data.
		Log.i("StepCounterService", "steps=" + steps);
		// tv.setText("Nr of steps: " + steps);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "StepCounterService starting", Toast.LENGTH_SHORT).show();

		Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

		// if we get killed, restart
		return START_STICKY;
		// Don't automatically restart this Service if it is killed
		// return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onDestroy() {
		mSensorManager.unregisterListener(this);
		super.onDestroy();
	}
}
