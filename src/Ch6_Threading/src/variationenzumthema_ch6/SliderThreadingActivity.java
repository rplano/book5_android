package variationenzumthema_ch6;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SliderThreadingActivity
 *
 * This activity shows how to implement the slider program with threads.
 *
 * Based on an example from Mehran Sahami's class Programming Methodology,
 * CS106A, https://see.stanford.edu/Course/CS106A
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SliderThreadingActivity extends GraphicsProgram {

	private final int SLIDER_SIZE = 80;
	private final int DELAY = 40;
	private final int STEP = 5;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		// needed on the emulator, otherwise it crashes...
		waitForTouch();
	}

	public void mousePressed(int x, int y) {
		// create a new slider
		Slider slider = new Slider(SLIDER_SIZE, rgen.nextColor());
		add(slider, 0, rgen.nextDouble(0, getHeight()));

		// run the slider in a new Thread
		Thread sliderThread = new Thread(slider);
		sliderThread.start();
	}

	private class Slider extends GRect implements Runnable {

		public Slider(int size, int color) {
			super(size / 2, size);
			setFilled(true);
			setFillColor(color);
		}

		public void run() {
			while (true) {
				pause(DELAY);
				move(STEP, 0);
			}
		}
	}

}
