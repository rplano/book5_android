package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: 5. Billiards
 * 
 * Animate a ball moving around inside the screen area.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Billiards extends GraphicsProgram {
	// constants
	// private final int WIDTH = 300;
	// private final int HEIGHT = 300;
	private final int BALL_SIZE = 80;
	private final int FONT_SIZE = 54;
	private final int DELAY = 10;

	// instance variables
	private GOval ball;
	private GLabel lbl;
	private int vx = 8;
	private int vy = -6;

	public void run() {
		waitForTouch();
		setup();

		// game loop
		while (true) {
			moveBall();
			checkForCollisionsWithWall();
			pause(DELAY);
		}
	}

	private void moveBall() {
		ball.move(vx, vy);
		lbl.move(vx, vy);
	}

	private void checkForCollisionsWithWall() {
		double x = ball.getX();
		double y = ball.getY();
		if ((x < 0) || (x > getWidth() - BALL_SIZE)) {
			vx = -vx;
		}
		if ((y < 0) || (y > getHeight() - BALL_SIZE)) {
			vy = -vy;
		}
	}

	private void setup() {
		// setSize(WIDTH, HEIGHT + 44);

		setBackground(Color.GREEN);

		ball = new GOval(BALL_SIZE, BALL_SIZE);
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball, getWidth() / 2, getHeight() / 2);
		lbl = new GLabel("8");
		lbl.setColor(Color.WHITE);
		lbl.setFont("Arial-bold-" + FONT_SIZE);
		add(lbl, getWidth() / 2 + 25, getHeight() / 2 + 55);
	}
}
