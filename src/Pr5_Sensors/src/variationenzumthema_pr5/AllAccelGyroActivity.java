package variationenzumthema_pr5;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AllAccelGyroActivity
 *
 * This activity displays the data of the accelerometer and gyroscope sensors
 * graphically.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AllAccelGyroActivity extends Activity implements SensorEventListener {

	// sensor related
	private SensorManager mSensorManager;

	private int attenuationAccel = 10;
	private int attenuationGyro = 10;
	private double[] accelLast = new double[3];
	private double[] gyroLast = new double[3];
	private double[] accelLastAvg = new double[3];
	private double[] gyroLastAvg = new double[3];

	// UI related
	private final int NR_OF_COLUMNS = 3;
	private final int NR_OF_ROWS = 2;
	private int width = 0;
	private int height = 0;

	private GraphView[] gvs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		// accel views
		for (int i = 0; i < 3; i++) {
			gvs[i].setMin(-0.5f);
			gvs[i].setMax(0.5f);
			gvs[i].setStyle(GraphView.GraphStyle.LINE);
			gvs[i].setColor(Color.RED);
			gvs[i].setStrokeWidth(1);
		}
		// gyro views
		for (int i = 3; i < 6; i++) {
			gvs[i].setMin(-0.1f);
			gvs[i].setMax(0.1f);
			gvs[i].setStyle(GraphView.GraphStyle.LINE);
			gvs[i].setColor(Color.BLUE);
			gvs[i].setStrokeWidth(1);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				double accel = event.values[i];
				double delta = accelLast[i] - accel;
				accelLast[i] = accel;
				accelLastAvg[i] = (attenuationAccel * accelLastAvg[i] + delta) / (attenuationAccel + 1);
				gvs[i].addDataPoint(accelLastAvg[i]);
				gvs[i].postInvalidate();
			}

			break;
		case Sensor.TYPE_GYROSCOPE:
			for (int i = 0; i < 3; i++) {
				double gyro = event.values[i];
				double delta = gyroLast[i] - gyro;
				gyroLast[i] = gyro;
				gyroLastAvg[i] = (attenuationGyro * gyroLastAvg[i] + delta) / (attenuationGyro + 1);
				gvs[i + 3].addDataPoint(gyroLastAvg[i]);
				gvs[i + 3].postInvalidate();
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

	private void createUI() {
		getScreenSize();

		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT);

		TableLayout tl = new TableLayout(this);
		tl.setBackgroundColor(0x200000ff);
		tl.setLayoutParams(tableParams);
		setContentView(tl);

		TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		rowParams.width = width / NR_OF_COLUMNS;
		rowParams.height = height / NR_OF_ROWS;

		gvs = new GraphView[6];

		TableRow tr1 = new TableRow(this);
		for (int i = 0; i < 3; i++) {
			gvs[i] = new GraphView(this);
			gvs[i].setLayoutParams(rowParams);
			tr1.addView(gvs[i]);
		}
		tl.addView(tr1);

		TableRow tr2 = new TableRow(this);
		for (int i = 3; i < 6; i++) {
			gvs[i] = new GraphView(this);
			gvs[i].setLayoutParams(rowParams);
			tr2.addView(gvs[i]);
		}
		tl.addView(tr2);
	}

	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		// display.getSize(size);
		display.getRealSize(size);
		int displayWidth = size.x;
		int displayHeight = size.y;
		Log.i("AllAccelGyroActivity", "width=" + width + ", height=" + height);

		// status bar height
		int statusBarHeight = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = getResources().getDimensionPixelSize(resourceId);
		}

		// action bar height
		int actionBarHeight = 0;
		final TypedArray styledAttributes = this.getTheme()
				.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
		actionBarHeight = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		// navigation bar height
		int navigationBarHeight = 0;
		resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
		}

		width = displayWidth;
		height = displayHeight - statusBarHeight - actionBarHeight - navigationBarHeight;
		Log.i("AllAccelGyroActivity", "width=" + width + ", height=" + height);
	}

}
