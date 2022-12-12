package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BrickBreakerProgram
 * 
 * This is a simple version of the Breakout/BrickBreakerProgram.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BrickBreakerProgram extends GraphicsProgram implements BrickBreakerConstants {

	private GOval ball;
	private GRect paddle;
	private double vx, vy;

	@Override
	public void run() {
		waitForTouch();
		setup();
		while (true) {
			moveBall();
			checkForCollisionsWithWall();
			checkForCollisions();
			pause(DELAY);
		}
	}

	private void moveBall() {
		ball.move((int) vx, (int) vy);
	}

	private void checkForCollisionsWithWall() {
		double xP = ball.getX();
		double yP = ball.getY();

		if ((xP < 0.0) || (xP > getWidth() - 2 * BALL_RADIUS)) {
			vx = -vx;
		}
		if ((yP < 0.0) || (yP > getHeight() - 2 * BALL_RADIUS)) {
			vy = -vy;
		}
	}

	private void checkForCollisions() {
		GObject collider = getCollidingObject();
		if (collider != null) {
			if (collider == paddle) {
				vy = -vy;
			} else if (collider != ball) { // must be brick
				vy = -vy;
				remove(collider);
			}
		}
	}

	private GObject getCollidingObject() {
		return getElementAt(ball);
	}

	private void setup() {
		// init velocity of ball:
		vx = getWidth() / 120;
		vy = getHeight() / 80;

		drawBricks();
		drawPaddle();
		drawBall();
	}

	private void drawBricks() {
		int y = BRICK_Y_OFFSET;
		int yOffset = 2 * (BRICK_HEIGHT + BRICK_SEP);
		createTwoRowsOfBrick(y, Color.RED);
		createTwoRowsOfBrick(y += yOffset, Color.MAGENTA);
		createTwoRowsOfBrick(y += yOffset, Color.YELLOW);
		createTwoRowsOfBrick(y += yOffset, Color.GREEN);
		createTwoRowsOfBrick(y += yOffset, Color.CYAN);
	}

	private void createTwoRowsOfBrick(int y, int col) {
		createOneRowOfBricks(y, col);
		createOneRowOfBricks(y += BRICK_HEIGHT + BRICK_SEP, col);
	}

	private void createOneRowOfBricks(int y, int col) {

		int w = (getWidth() - BRICK_SEP * (NBRICK_ROWS + 1)) / NBRICK_ROWS;
		for (int i = 0; i < NBRICK_ROWS; i++) {
			int x = i * (w + BRICK_SEP) + BRICK_SEP;
			GRect brick = new GRect(x, y, w, BRICK_HEIGHT);
			brick.setColor(col);
			brick.setFilled(true);
			brick.setFillColor(col);
			add(brick);
		}
	}

	private void drawBall() {
		ball = new GOval(getWidth() / 2, getHeight() / 2, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		add(ball);
	}

	private void drawPaddle() {
		paddle = new GRect((getWidth() - PADDLE_WIDTH) / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		add(paddle);
	}

	public void mouseMoved(int x, int y) {
		if (paddle != null) {
			paddle.setX(x);
		}
	}
}
