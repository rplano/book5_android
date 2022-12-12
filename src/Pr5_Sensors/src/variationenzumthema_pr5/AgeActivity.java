package variationenzumthema_pr5;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AgeActivity
 *
 * This activity measures how much you tremble and assuming age is related to
 * trembling estimates your age.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AgeActivity extends Activity implements SensorEventListener {
	private final int ATTENUATION = 10;
	private final int DELAY = 1000;

	// sensor related
	private SensorManager mSensorManager;

	private double[] accelLast = new double[3];
	private double[] deltaAvg = new double[3];

	private long lastTime = System.currentTimeMillis();
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	private void checkForAge() {
		long thisTime = System.currentTimeMillis();
		if (thisTime - lastTime > DELAY) {
			double maxDeltaAvg = Math.max(deltaAvg[2], Math.max(deltaAvg[1], deltaAvg[0]));
			int age = (int) (maxDeltaAvg * 100);
			tv.setText("You are about: " + age + " years old!");
			lastTime = thisTime;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				double accel = event.values[i];
				double delta = Math.abs(accelLast[i] - accel);
				accelLast[i] = accel;
				deltaAvg[i] = (ATTENUATION * deltaAvg[i] + delta) / (ATTENUATION + 1);
				checkForAge();
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

	private void createUI() {
		tv = new TextView(this);
		tv.setText("Your age is: calculating...");
		tv.setGravity(Gravity.TOP | Gravity.LEFT);
		tv.setSingleLine(false);
		tv.setVerticalScrollBarEnabled(true);
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setTextSize(20);
		tv.setPadding(5, 5, 5, 5);
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(tv);
	}

}
