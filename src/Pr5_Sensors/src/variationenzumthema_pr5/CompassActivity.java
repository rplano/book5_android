package variationenzumthema_pr5;

import acm_graphics.GLabel;
import acm_graphics.GOval;
import acm_graphics.GPolygon;
import acm_graphics.GraphicsProgram;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * CompassActivity
 *
 * This activity implements a magnetic compass with an analog display. It uses
 * the TYPE_ROTATION_VECTOR sensor as magnetic sensor.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CompassActivity extends GraphicsProgram implements SensorEventListener {
	private final int DELAY = 100;
	private final int FONT_SIZE = 36;

	private int OFFSET = 300;
	private int SIZE = 300;

	private SensorManager mSensorManager;
	private GSpaceShip ship;
	private GOval face;

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
		SIZE = getWidth();
		OFFSET = (getHeight() - SIZE) / 2;
		drawFace();
		drawAngles();
	}

	private void drawSpaceShip(float degrees) {
		if (face != null) {
			if (ship != null) {
				remove(ship);
			}
			ship = new GSpaceShip();
			ship.rotate(-degrees);
			add(ship, SIZE / 2, SIZE / 2 + OFFSET);
		}
	}

	private void drawAngles() {
		for (int i = -5; i <= 6; i++) {
			GLabel digit = new GLabel("" + i * 30);
			digit.setFont("Times New Roman-bold-" + FONT_SIZE);
			double radians = 2 * Math.PI * i / 12;
			double radius = SIZE - 80;
			double x = -30 + SIZE / 2 + Math.sin(radians) * radius / 2;
			double y = 10 + SIZE / 2 - Math.cos(radians) * radius / 2;
			add(digit, x, y + OFFSET);
		}
	}

	private void drawFace() {
		face = new GOval(SIZE, SIZE);
		add(face, 0, OFFSET);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float degrees = event.values[2] * 180;
		Log.i("degrees=", "" + degrees);
		drawSpaceShip(degrees);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Sensor mMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		if (mMagnet != null) {
			mSensorManager.registerListener(this, mMagnet, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			Toast.makeText(this, "Your device has no magnetic sensors!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	class GSpaceShip extends GPolygon {

		public static final int SPACE_SHIP_SIZE = 200;

		public double vx = 0.0;
		public double vy = 0.0;
		public double angle = 0.0;

		public GSpaceShip() {
			super();
			this.addVertex(0, -SPACE_SHIP_SIZE);
			this.addVertex(-2 * SPACE_SHIP_SIZE / 3, SPACE_SHIP_SIZE);
			this.addVertex(0, SPACE_SHIP_SIZE / 2);
			this.addVertex(2 * SPACE_SHIP_SIZE / 3, SPACE_SHIP_SIZE);
		}

		public void rotate(double theta) {
			super.rotate(theta);
			angle += theta;
		}
	}
}
