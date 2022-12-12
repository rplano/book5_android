package variationenzumthema_ch5;

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
 * StepCounterActivity
 *
 * This activity demonstrates how to use the TYPE_STEP_COUNTER sensor.
 *
 * @see https://github.com/googlesamples/android-BatchStepSensor/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class StepCounterActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private TextView tv;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		float steps = event.values[0];
		tv.setText("Nr of steps: " + steps);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	private void createUI() {
		tv = new TextView(this);
		tv.setText("");
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