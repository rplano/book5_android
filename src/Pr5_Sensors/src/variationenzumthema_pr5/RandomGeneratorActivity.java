package variationenzumthema_pr5;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RandomGeneratorActivity
 *
 * This activity creates the seed for the RandomGenerator from the accelerometer
 * and gyroscope sensors.
 * 
 * You might want to remove the first digit and in addition do a SHA-256...
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RandomGeneratorActivity extends Activity implements SensorEventListener {

	private TextView tv;
	private RandomGenerator rgen;

	private float[] acceleration_in_m_per_s2 = new float[3];
	private float[] rotation_in_rad_per_s = new float[3];

	private SensorManager mSensorManager;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.random_generator_activity);

		rgen = new RandomGenerator();

		tv = (TextView) findViewById(R.id.textview);

		Button btnInit = (Button) findViewById(R.id.btnInit);
		btnInit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float seedA = acceleration_in_m_per_s2[0];
				seedA += acceleration_in_m_per_s2[1];
				// gravity is to predictable:
				float gravity = acceleration_in_m_per_s2[2];
				// get rid of first 3 digits
				gravity = gravity * 100 - (int) (gravity * 100);
				seedA += gravity;

				float seedB = rotation_in_rad_per_s[0];
				seedB += rotation_in_rad_per_s[1];
				seedB += rotation_in_rad_per_s[2];
				Log.i("seedA: ", "" + seedA);
				Log.i("seedB: ", "" + seedB);

				// get absolute value:
				seedA = Math.abs(seedA);
				seedB = Math.abs(seedB);
				if (seedA > 0 && seedB > 0) {
					// get exponent:
					int expA = (int) Math.log10(seedA);
					int expB = (int) Math.log10(seedB);
					seedA = (float) (seedA * Math.pow(10, -expA));
					seedB = (float) (seedB * Math.pow(10, -expB));
					Log.i("seedA: ", "" + seedA);
					Log.i("seedB: ", "" + seedB);

					// now make them big
					long seed = (long) (seedB * 100000 * 100000) + (long) (seedA * 100000);

					rgen.setSeed((int) seed);
					tv.setText("" + seed);

				} else {
					Toast.makeText(v.getContext(), "Bad seeds, try again!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv.setText("" + rgen.nextInt());
			}
		});

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			acceleration_in_m_per_s2 = event.values;
			break;
		case Sensor.TYPE_GYROSCOPE:
			rotation_in_rad_per_s = event.values;
			break;
		default:
			return;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (mAccelerometer != null) {
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		}

		Sensor mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		if (mGyroscope != null) {
			mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
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