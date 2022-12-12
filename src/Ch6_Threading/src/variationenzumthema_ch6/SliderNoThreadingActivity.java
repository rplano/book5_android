package variationenzumthema_ch6;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SliderNoThreadingActivity
 *
 * This activity shows how to implement the slider program without threading,
 * using a game loop.
 *
 * Based on an example from Mehran Sahami's class Programming Methodology,
 * CS106A, https://see.stanford.edu/Course/CS106A
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SliderNoThreadingActivity extends GraphicsProgram {

	private final int SLIDER_NR = 10;
	private final int SLIDER_SIZE = 80;
	private final int SLIDER_SEPERATION = 20;
	private final int DELAY = 40;
	private final int STEP = 5;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		waitForTouch();
		Slider[] sliders = createSliders();

		// game loop
		while (true) {
			for (int i = 0; i < sliders.length; i++) {
				int dx = (int) (Math.random() * STEP );
				sliders[i].move(dx, 0);
			}
			pause(DELAY);
		}
	}

	private Slider[] createSliders() {
		Slider[] sliders;
		sliders = new Slider[SLIDER_NR];
		for (int i = 0; i < sliders.length; i++) {
			sliders[i] = new Slider(SLIDER_SIZE, rgen.nextColor());
			add(sliders[i], 50, SLIDER_SEPERATION + i * (SLIDER_SIZE + SLIDER_SEPERATION));
		}
		return sliders;
	}

	private class Slider extends GRect {
		public Slider(int size, int color) {
			super(size / 2, size);
			setFilled(true);
			setFillColor(color);
		}
	}
}
