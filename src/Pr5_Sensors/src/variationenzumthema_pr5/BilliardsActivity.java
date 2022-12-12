package variationenzumthema_pr5;

import acm_graphics.*;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BilliardsActivity
 * 
 * Animate a ball moving around inside the screen area, based on acceleration sensor.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BilliardsActivity extends GraphicsProgram implements SensorEventListener{
	// constants
	// private final int WIDTH = 300;
	// private final int HEIGHT = 300;
	private final int BALL_SIZE = 80;
	private final int FONT_SIZE = 54;
	private final int DELAY = 10;

	// instance variables
	private GOval ball;
	private GLabel lbl;
	private int vx = 0;
	private int vy = 0;

	private SensorManager mSensorManager;

	public void init() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}
	
	public void run() {
		waitForTouch();
		setup();

		// game loop
		while (true) {
			moveBall();
			checkForCollisionsWithWall();
			pause(DELAY);
		}
	}

	private void moveBall() {
		ball.move(vx, vy);
		lbl.move(vx, vy);
	}

	private void checkForCollisionsWithWall() {
		double x = ball.getX();
		double y = ball.getY();
		if ((x < 0) || (x > getWidth() - BALL_SIZE)) {
			vx = -vx;
		}
		if ((y < 0) || (y > getHeight() - BALL_SIZE)) {
			vy = -vy;
		}
	}

	private void setup() {
		setBackground(Color.GREEN);

		ball = new GOval(BALL_SIZE, BALL_SIZE);
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball, getWidth() / 2, getHeight() / 2);
		lbl = new GLabel("8");
		lbl.setColor(Color.WHITE);
		lbl.setFont("Arial-bold-" + FONT_SIZE);
		add(lbl, getWidth() / 2 + 25, getHeight() / 2 + 55);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float[] acceleration_in_m_per_s2 = event.values;
			vx -= acceleration_in_m_per_s2[0];
			vy += acceleration_in_m_per_s2[1];			
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
