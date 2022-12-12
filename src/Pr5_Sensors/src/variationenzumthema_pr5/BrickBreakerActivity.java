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
 * BrickBreakerActivity
 * 
 * In BrickBreaker we want the paddle to be controlled by the acceleration sensor.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BrickBreakerActivity extends GraphicsProgram implements SensorEventListener {

	public static final int PADDLE_WIDTH = 200;
	public static final int PADDLE_HEIGHT = 20;
	public static final int PADDLE_Y_OFFSET = 80;

	public static final int DELAY = 40; // 25 fps
	public static final int BALL_RADIUS = 20;

	public static final int NBRICKS_PER_ROW = 10;
	public static final int NBRICK_ROWS = 10;
	public static final int BRICK_SEP = 4;
	public static final int BRICK_HEIGHT = 20;
	public static final int BRICK_Y_OFFSET = 80;

	private GOval ball;
	private GRect paddle;
	private double vx, vy;
	private double paddleSpeed = 0;

	private SensorManager mSensorManager;

	public void init() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public void run() {
		waitForTouch();
		setup();
		while (true) {
			moveBall();
			movePaddle();
			checkForCollisionsWithWall();
			checkForCollisions();
			pause(DELAY);
		}
	}

	private void movePaddle() {
		if (paddle != null) {
			double xP = paddle.getX();

			if (xP < 0.0) {
				paddleSpeed = 0;
				paddle.setX(1);
			} else if (xP > getWidth() - 2 * BALL_RADIUS) {
				paddleSpeed = 0;
				paddle.setX(getWidth() - 2 * BALL_RADIUS - 1);
			}
			paddle.move((int) paddleSpeed, 0);
		}
	}

	private void moveBall() {
		ball.move((int) vx, (int) vy);
	}

	private void checkForCollisionsWithWall() {
		double xP = ball.getX();
		double yP = ball.getY();

		if ((xP < 0.0) || (xP > getWidth() - 2 * BALL_RADIUS)) {
			vx = -vx;
		}
		if ((yP < 0.0) || (yP > getHeight() - 2 * BALL_RADIUS)) {
			vy = -vy;
		}
	}

	private void checkForCollisions() {
		GObject collider = getCollidingObject();
		if (collider != null) {
			if (collider == paddle) {
				vy = -vy;
			} else if (collider != ball) { // must be brick
				vy = -vy;
				remove(collider);
			}
		}
	}

	private GObject getCollidingObject() {
		return getElementAt(ball);
	}

	private void setup() {
		// init velocity of ball:
		vx = getWidth() / 120;
		vy = getHeight() / 80;

		drawBricks();
		drawPaddle();
		drawBall();
	}

	private void drawBricks() {
		int y = BRICK_Y_OFFSET;
		int yOffset = 2 * (BRICK_HEIGHT + BRICK_SEP);
		createTwoRowsOfBrick(y, Color.RED);
		createTwoRowsOfBrick(y += yOffset, Color.MAGENTA);
		createTwoRowsOfBrick(y += yOffset, Color.YELLOW);
		createTwoRowsOfBrick(y += yOffset, Color.GREEN);
		createTwoRowsOfBrick(y += yOffset, Color.CYAN);
	}

	private void createTwoRowsOfBrick(int y, int col) {
		createOneRowOfBricks(y, col);
		createOneRowOfBricks(y += BRICK_HEIGHT + BRICK_SEP, col);
	}

	private void createOneRowOfBricks(int y, int col) {

		int w = (getWidth() - BRICK_SEP * (NBRICK_ROWS + 1)) / NBRICK_ROWS;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			int x = i * (w + BRICK_SEP) + BRICK_SEP;
			GRect brick = new GRect(x, y, w, BRICK_HEIGHT);
			brick.setColor(col);
			brick.setFilled(true);
			brick.setFillColor(col);
			add(brick);
		}
	}

	private void drawBall() {
		ball = new GOval(getWidth() / 2, getHeight() / 2, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
	}

	private void drawPaddle() {
		paddle = new GRect((getWidth() - PADDLE_WIDTH) / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}

	// public void mouseMoved(int x, int y) {
	// if (paddle != null) {
	// paddle.setX(x);
	// }
	// }

	@Override
	public final void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			float[] acceleration_in_m_per_s2 = event.values;
			paddleSpeed -= acceleration_in_m_per_s2[0];
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
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
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
