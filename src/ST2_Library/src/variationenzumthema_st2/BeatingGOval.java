package variationenzumthema_st2;

import acm_graphics.GOval;
import acm_graphics.GraphicsProgram;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BeatingGOval
 * 
 * Animate the size of a GOval.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BeatingGOval extends GraphicsProgram {
	// constants
	private final int BALL_MIN_SIZE = 80;
	private final int BALL_MAX_SIZE = 500;
	private final int DELAY = 10;

	// instance variables
	private GOval ball;
	private int delta = +2;

	public void run() {
		waitForTouch();
		setup();

		// game loop
		while (true) {
			changeBallSize(delta);
			pause(DELAY);
			if (ball.getWidth() > BALL_MAX_SIZE) {
				delta = -2;
			} else if (ball.getWidth() < BALL_MIN_SIZE) {
				delta = +2;
			}
		}
	}

	private void changeBallSize(int deltaI) {
		int x = ball.getX() - deltaI;
		int y = ball.getY() - deltaI;
		int w = ball.getWidth() + 2 * deltaI;
		int h = ball.getHeight() + 2 * deltaI;
		ball.setBounds(x, y, w, h);
	}

	private void setup() {
		setBackground(Color.WHITE);

		ball = new GOval(BALL_MIN_SIZE, BALL_MIN_SIZE);
		ball.setFillColor(Color.GREEN);
		ball.setFilled(true);
		add(ball, getWidth() / 2, getHeight() / 2);
	}
}
