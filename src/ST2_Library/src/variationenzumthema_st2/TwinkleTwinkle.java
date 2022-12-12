package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: TwinkleTwinkle
 * 
 * Draw a random star map.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TwinkleTwinkle extends GraphicsProgram {
	private final int DELAY = 500;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		//this.setSize(WIDTH, HEIGHT);
		setBackground(Color.BLACK);

		while (true) {
			int x = rgen.nextInt(getWidth());
			int y = rgen.nextInt(getHeight());
			int size = rgen.nextInt(1, 20);
			GOval star = new GOval(size, size);
			star.setFillColor(Color.WHITE);
			star.setFilled(true);
			add(star, x, y);

			pause(DELAY);
		}
	}
}
