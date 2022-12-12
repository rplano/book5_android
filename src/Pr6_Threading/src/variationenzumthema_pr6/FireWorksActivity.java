package variationenzumthema_pr6;

import acm_graphics.*;
import android.app.ActivityManager;
import android.graphics.Color;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * FireWorksActivity
 *
 * This activity that simulates fireworks using threads.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FireWorksActivity extends GraphicsProgram {
	private final int SIZE = 20;
	private final int SPEED = 4;
	private final int NR_LIGHTS = 10;
	private final int DELAY = 200;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		setBackground(Color.BLACK);
		waitForTouch();

		while (true) {
			startExplosion();
			pause(DELAY);
			Log.i("FireWorksActivity", "availMem=" + getAvailableMemory().availMem);
		}
	}

	private void startExplosion() {
		int col = rgen.nextBrightColor();
		int x = rgen.nextInt(0, getWidth());
		int y = rgen.nextInt(0, getHeight());
		double angle = Math.random(); // start with a random angle
		double deltaAngle = 2.0 * Math.PI / NR_LIGHTS;
		for (int i = 0; i < NR_LIGHTS; i++) {
			double vx = SPEED * Math.cos(angle);
			double vy = SPEED * Math.sin(angle);

			GLight light = new GLight(vx, vy, col);
			add(light, x, y);

			Thread thread = new Thread(light);
			thread.start();

			angle += deltaAngle;
		}
	}

	// Get a MemoryInfo object for the device's current memory status.
	private ActivityManager.MemoryInfo getAvailableMemory() {
		ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		return memoryInfo;
	}

	private class GLight extends GOval implements Runnable {
		private static final int DELAY = 40;
		private static final double GRAVITY = 0.05;

		double vx;
		double vy;

		public GLight(double vx, double vy, int col) {
			super(SIZE, SIZE);
			setFillColor(col);
			setFilled(true);
			this.vx = vx;
			this.vy = vy;
		}

		@Override
		public void run() {
			// animate the light
			for (int i = 0; i < 100; i++) {
				pause(DELAY);
				move((int) vx, (int) vy);
				vy = vy + GRAVITY;
			}
			// make the light invisible
			setFillColor(Color.BLACK);
		}
	}

}
