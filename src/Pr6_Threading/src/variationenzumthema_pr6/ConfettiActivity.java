package variationenzumthema_pr6;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ConfettiActivity
 *
 * This activity simulates confetti falling down using threads.
 * 
 * Notice all the confetti collecting at the bottom, these are all dead threads!
 * Drawing, i.e. the onDraw() method could be problematic. because the UI thread
 * (gview) calls our Confetti thread! Thats why we have synchronized (gobjects)
 * {...} there! When to many confetti, then we get a dead-lock, because we are
 * not able to draw all the confetti in the allotted time!
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ConfettiActivity extends GraphicsProgram {
	private final int SIZE = 40;
	private final int STEP = 2;
	private final int DELAY_CREATION = 1000;
	private final int DELAY_MOVE = 20;

	private int HEIGHT = 1000;
	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		waitForTouch();
		HEIGHT = getHeight();
		while (true) {
			// create a new random confetti
			int width = rgen.nextInt(SIZE / 2, SIZE);
			int col = rgen.nextColor();
			double x = rgen.nextDouble(-SIZE / 2, getWidth());
			double y = rgen.nextDouble(-SIZE / 2, 100);
			Confetti confetti = new Confetti(width, col);
			add(confetti, x, y);

			// run the confetti in a new Thread
			Thread confettiThread = new Thread(confetti);
			confettiThread.start();

			pause(DELAY_CREATION);
		}

	}

	private class Confetti extends GOval implements Runnable {

		public Confetti(int width, int col) {
			super(width, width);
			setFilled(true);
			setFillColor(col);
		}

		@Override
		public void run() {
			// animate the slide across the screen
			for (int i = 0; i < HEIGHT / STEP; i++) {
				pause(DELAY_MOVE);
				int x = (int) (Math.random() * 2 - 1);
				move(x, STEP);
			}
		}

	}
}
