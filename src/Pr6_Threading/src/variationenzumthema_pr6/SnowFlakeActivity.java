package variationenzumthema_pr6;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SnowFlakeActivity
 *
 * This activity simulates snow flakes falling down using threads.
 *
 * You may notice the dead lock occurs much earlier than in the confetti
 * example, because snow flakes are more complicated to draw.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SnowFlakeActivity extends GraphicsProgram {
	private final int SIZE = 60;
	private final int DELAY_CREATION = 100;
	private final int DELAY_MOVE = 40;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		waitForTouch();

		while (true) {
			// create a new random confetti
			int width = rgen.nextInt(SIZE / 2, SIZE);
			SnowFlake snowFlake = new SnowFlake(width);
			double x = rgen.nextDouble(-SIZE / 2, getWidth());
			double y = rgen.nextDouble(-SIZE / 2, getHeight());
			add(snowFlake, x, y);

			// run the confetti in a new Thread
			Thread thread = new Thread(snowFlake);
			thread.start();

			pause(DELAY_CREATION);
		}

	}

	private class SnowFlake extends GPolygon implements Runnable {

		private static final int STEP = 1;
		private final int NR_OF_ITERATIONS = 4;

		public SnowFlake(int size) {
			super();
			createKochSnowflake(0, 0, size, NR_OF_ITERATIONS);
		}

		private void createKochSnowflake(int x, int y, int length, int nrOfIterations) {
			drawKochLine(x, y, length, 0, nrOfIterations);
			drawKochLine(x + length, y, length, -120, nrOfIterations);
			double x1 = x + length * Math.cos(-60 * Math.PI / 180);
			double y1 = y - length * Math.sin(-60 * Math.PI / 180);
			drawKochLine(x1, y1, length, 120, nrOfIterations);
		}

		private void drawKochLine(double x0, double y0, double length, double angle, int nrOfIterations) {
			// base case:
			if (nrOfIterations == 0) {
				double x1 = x0 + length * Math.cos(angle * Math.PI / 180);
				double y1 = y0 - length * Math.sin(angle * Math.PI / 180);
				// GLine line = new GLine(x0, y0, x1, y1);
				// add(line);
				addVertex((int) x1, (int) y1);
				return;

				// recursive case:
			} else {
				double len = length / 3;
				double ang = angle;
				drawKochLine(x0, y0, len, ang + 0, nrOfIterations - 1);
				double x1 = x0 + len * Math.cos(ang * Math.PI / 180);
				double y1 = y0 - len * Math.sin(ang * Math.PI / 180);
				drawKochLine(x1, y1, len, ang + 60, nrOfIterations - 1);
				ang = ang + 60;
				double x2 = x1 + len * Math.cos(ang * Math.PI / 180);
				double y2 = y1 - len * Math.sin(ang * Math.PI / 180);
				drawKochLine(x2, y2, len, ang - 120, nrOfIterations - 1);
				ang = ang - 120;
				double x3 = x2 + len * Math.cos(ang * Math.PI / 180);
				double y3 = y2 - len * Math.sin(ang * Math.PI / 180);
				drawKochLine(x3, y3, len, ang + 60, nrOfIterations - 1);
			}
		}

		@Override
		public void run() {
			// animate the slide across the screen
			for (int i = 0; i < 1000 / STEP; i++) {
				pause(DELAY_MOVE);
				int x = (int) (Math.random() * 2 - 1);
				move(x, STEP);
			}
		}
	}
}
