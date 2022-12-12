package variationenzumthema_pr5;

import acm_graphics.GOval;
import acm_graphics.GRect;
import acm_graphics.GraphicsProgram;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BubbleLevelActivity
 * 
 * This activity demonstrates how to use the acceleration sensor to implement a
 * simple bubble level.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BubbleLevelActivity extends GraphicsProgram implements SensorEventListener {
	// constants
	private final int BUBBLE_SIZE = 80;
	private final int PADDING = BUBBLE_SIZE;
	private final int SENSITIVITY = 100;
	private final int DELAY = 40;

	// instance variables
	private GOval bubbleHor;
	private GOval bubbleVer;

	private SensorManager mSensorManager;

	public void init() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	public void run() {
		waitForTouch();
		setup();

		// game loop
		while (true) {
			pause(DELAY);
		}
	}

	private void setup() {
		setBackground(Color.GREEN);

		GRect housingHor = new GRect(getWidth() - PADDING * 4, BUBBLE_SIZE);
		housingHor.setFillColor(Color.YELLOW);
		housingHor.setFilled(true);
		add(housingHor, 2 * PADDING, PADDING);

		GRect housingHorZero = new GRect(BUBBLE_SIZE, BUBBLE_SIZE);
		housingHorZero.setFillColor(Color.YELLOW);
		housingHorZero.setFilled(true);
		add(housingHorZero, (getWidth() - BUBBLE_SIZE) / 2, PADDING);

		bubbleHor = new GOval(BUBBLE_SIZE, BUBBLE_SIZE);
		bubbleHor.setFillColor(Color.WHITE);
		bubbleHor.setFilled(true);
		add(bubbleHor, (getWidth() - BUBBLE_SIZE) / 2, PADDING);

		GRect housingVer = new GRect(BUBBLE_SIZE, getHeight() - PADDING * 4);
		housingVer.setFillColor(Color.YELLOW);
		housingVer.setFilled(true);
		add(housingVer, PADDING, 2 * PADDING);

		GRect housingVerZero = new GRect(BUBBLE_SIZE, BUBBLE_SIZE);
		housingVerZero.setFillColor(Color.YELLOW);
		housingVerZero.setFilled(true);
		add(housingVerZero, PADDING, (getHeight() - BUBBLE_SIZE) / 2);

		bubbleVer = new GOval(BUBBLE_SIZE, BUBBLE_SIZE);
		bubbleVer.setFillColor(Color.WHITE);
		bubbleVer.setFilled(true);
		add(bubbleVer, PADDING, (getHeight() - BUBBLE_SIZE) / 2);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float[] acceleration_in_m_per_s2 = event.values;
			if (bubbleHor != null) {
				int x = (int) ((getWidth() - BUBBLE_SIZE) / 2 + acceleration_in_m_per_s2[0] * SENSITIVITY);
				bubbleHor.setX(x);
				int y = (int) ((getHeight() - BUBBLE_SIZE) / 2 - acceleration_in_m_per_s2[1] * SENSITIVITY);
				bubbleVer.setY(y);
			}
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
