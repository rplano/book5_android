package variationenzumthema_st2;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: Confetti
 * 
 * Draw confetti on the screen.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Confetti extends GraphicsProgram {
	private final int SIZE = 40;
	private final int DELAY = 40;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		//this.setSize(WIDTH, HEIGHT);

		while (true) {
			// create randomly sized rect
			int width = rgen.nextInt(SIZE / 2, SIZE);
			GOval rect = new GOval(width, width);

			// set random color of rect
			rect.setFilled(true);
			rect.setFillColor(rgen.nextInt());

			// put rect at random position
			double x = rgen.nextDouble(-SIZE / 2, getWidth());
			double y = rgen.nextDouble(-SIZE / 2, getHeight());
			add(rect, x, y);

			pause(DELAY);
		}
	}
}
