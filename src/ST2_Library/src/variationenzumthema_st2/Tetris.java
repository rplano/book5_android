package variationenzumthema_st2;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: Tetris
 * 
 * Play a simple version of Tetris.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Tetris extends GraphicsProgram {
	private final int BRICK_SIZE = 80;

//	private final int WIDTH = BRICK_SIZE * 10;
//	private final int HEIGHT = BRICK_SIZE * 10 + 15;

	private final int DELAY = 500;

	private RandomGenerator rgen = new RandomGenerator();

	private GRect brick;
	private int vy = BRICK_SIZE;

	public void run() {
		waitForTouch();
		setup();

		while (true) {
			moveBrick();
			checkForCollision();
			pause(DELAY);
		} 
	}

//	public void keyPressed(KeyEvent e) {
	public void mousePressed(int x, int y) {
		//Log.i("Tetris", "mousePressed");
		if (y > getHeight() / 2) {
			brick.move(0, BRICK_SIZE);
		} else {
			if (x < getWidth() / 2) {
				brick.move(-BRICK_SIZE, 0);
			} else {
				brick.move(BRICK_SIZE, 0);
			}
		}
//		switch (e.getKeyCode()) {
//		case 38:
//			// move up
//			break;
//		case 40:
//			// move down
//			brick.move(0, BRICK_SIZE);
//			break;
//		case 37:
//			// move left
//			brick.move(-BRICK_SIZE, 0);
//			break;
//		case 39:
//			// move right
//			brick.move(BRICK_SIZE, 0);
//			break;
//		}
	}

	private void checkForCollision() {
		// check bottom
		if (brick.getY() > getHeight() - brick.getHeight()) {
			createNewBrick();
		}
		// check for other bricks
		GObject obj = getElementAt(brick.getX() + 1,
				brick.getY() + brick.getHeight());
		if ((obj != null) && (obj != brick)) {
			createNewBrick();
			return;
		} 
		obj = getElementAt(brick.getX() + brick.getWidth() - 1, brick.getY()
				+ brick.getHeight());
		if ((obj != null) && (obj != brick)) {
			createNewBrick();
			return;
		}
	}

	private void moveBrick() {
		brick.move(0, vy);
	}

	private void setup() {
		//setSize(WIDTH, HEIGHT);
		createNewBrick();
		//addKeyListeners();
	}

	private void createNewBrick() {
		switch (rgen.nextInt(0, 3)) {
		case 0:
			brick = new GRect(getWidth() / 2, 0, BRICK_SIZE, BRICK_SIZE * 2);
			break;
		case 1:
			brick = new GRect(getWidth() / 2, 0, BRICK_SIZE * 2, BRICK_SIZE);
			break;
		case 2:
			brick = new GRect(getWidth() / 2, 0, BRICK_SIZE * 2, BRICK_SIZE * 2);
			break;
		default:
			brick = new GRect(getWidth() / 2, 0, BRICK_SIZE, BRICK_SIZE);
			break;
		}
		brick.setFilled(true);
		brick.setFillColor(rgen.nextColor());
		add(brick);
		//brick.sendToBack();
	}

}
