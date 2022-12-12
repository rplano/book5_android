package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Asteroids: GeometryRun
 * 
 * A jump-and-run game where you try to escape incoming geometric objects.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GeometryRun extends GraphicsProgram implements GeometryConstants {

	public int DASH_X_POS;
	public int DASH_Y_POS;

	private RandomGenerator rgen = new RandomGenerator();

	private GeometryObstacle[] obstacles;
	private Geometry runner;

	public void run() {
		waitForTouch();
		setup();
		while (true) {
			moveObstacles();
			moveDash();
			checkForCollision();
			pause(DELAY);
		}
	}

	private void moveDash() {
		if (runner.getY() < DASH_Y_POS) {
			runner.vy += GRAVITY;
			runner.move();
		} else {
			runner.vy = 0;
			runner.setLocation(DASH_X_POS, DASH_Y_POS);
		}
	}

	private void checkForCollision() {
		for (int i = 0; i < obstacles.length; i++) {
			if (obstacles[i].getX() < 0) {
				obstacles[i].setLocation(getWidth(), obstacles[i].getY());
			}
		}
	}

	// public void keyPressed(KeyEvent e) {
	// int code = e.getKeyCode();
	// if (code == ' ') {
	// runner.vy -= DASH_JUMP;
	// runner.move();
	// }
	// }
	@Override
	public void mousePressed(int x, int y) {
		runner.vy -= DASH_JUMP;
		runner.move();
	}

	private void moveObstacles() {
		for (int i = 0; i < obstacles.length; i++) {
			obstacles[i].move();
		}
	}

	private void setup() {
		// setSize(APP_WIDTH, APP_HEIGHT);
		// addKeyListeners();
		DASH_X_POS = getWidth() / 2;
		DASH_Y_POS = getHeight() - 2 * OBSTACLES_SIZE;

		runner = new Geometry();
		runner.setFilled(true);
		runner.setColor(Color.GREEN);
		add(runner, DASH_X_POS, DASH_Y_POS);

		obstacles = new GeometryObstacle[NR_OF_OBSTACLES];
		for (int i = 0; i < obstacles.length; i++) {
			int x = rgen.nextInt(getWidth());
			int y = getHeight() - 2 * OBSTACLES_SIZE;
			obstacles[i] = new GeometryObstacle();
			obstacles[i].vx = -OBSTACLES_SPEED;
			add(obstacles[i], x, y);
		}
	}
}
