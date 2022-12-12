package variationenzumthema_pr6;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RaceHorseActivity
 *
 * This activity simulates race horses to show the issues that can occur in
 * multi-threaded programming.
 *
 * Based on an example from Mehran Sahami's class Programming Methodology,
 * CS106A, https://see.stanford.edu/Course/CS106A
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RaceHorseActivity extends GraphicsProgram {
	private final int NR_OF_HORSES = 10;
	private final int HORSE_SIZE = 120;

	private final int STEP = 10;
	private final int LENGTH = 800;

	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		waitForTouch();

		boolean[] isThereAWinner = new boolean[1];
		isThereAWinner[0] = false;

		// create ten horses
		RaceHorse[] rhs = new RaceHorse[NR_OF_HORSES];
		for (int i = 0; i < rhs.length; i++) {
			rhs[i] = new RaceHorse(HORSE_SIZE / 3, HORSE_SIZE, isThereAWinner);
			add(rhs[i], 100, 50 + ((HORSE_SIZE + 20) * i));
		}

		GThickLine finishLine = new GThickLine((100 + LENGTH) - 2, 0, (100 + LENGTH) - 2, getHeight(), 4);
		add(finishLine);

		// turn them into threads
		Thread[] ths = new Thread[NR_OF_HORSES];
		for (int i = 0; i < rhs.length; i++) {
			ths[i] = new Thread(rhs[i]);
		}

		// start them of
		for (int i = 0; i < rhs.length; i++) {
			ths[i].start();
		}
	}

	private class RaceHorse extends GRect implements Runnable {
		private boolean[] isThereAWinner;

		public RaceHorse(double w, double h, boolean[] isThereAWinner) {
			super((int) w, (int) h);
			this.isThereAWinner = isThereAWinner;
		}

		@Override
		public void run() {
			for (int i = 0; i < (LENGTH / STEP); i++) {
				move(STEP, 0);
				pause(rgen.nextInt(50, 300));
				// pause(100);
			}
			// reached finish, check if I am first
			synchronized (isThereAWinner) {
				if (!isThereAWinner[0]) {
					// pause(200);
					isThereAWinner[0] = true;
					setFilled(true);
					setFillColor(Color.RED);
				}
			}
			invalidateView();
		}

	}

}
