package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Asteroids: FlappyBall
 * 
 * Inspired by its big cousin. ToDo: dont touch floor!
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */ 
public class FlappyBall extends GraphicsProgram {
//	private final int WIDTH = 400;
//	private final int HEIGHT = 400;

	private final int DELAY = 20;
	private final double GRAVITY = 1;

	private final int WALL_WIDTH = 60;

	private final int BALL_DIAM = 60;
	private final int BALL_OFFSET = 4;
	private static final double BALL_ACCEL = -10.0;

	private RandomGenerator rgen = new RandomGenerator();

	private boolean alive = true;
	private int wallCounter = 0;
	private double ballVel = 0.0;
	private double wallVel = 1.5;
	private GOval ball;
	private GRect upperWall;
	private GRect lowerWall;

	public void run() {
		waitForTouch();
		setup();
		
		while (alive) {
			moveBall();
			moveWall();
			checkForCollision();
			pause(DELAY);
		}
		//println("Game over!  You survived " + wallCounter + " walls.");
	}

	/** Update and move ball */
	private void moveBall() {
		// increase yVelocity due to gravity on each cycle
		ball.move(0, (int)ballVel);
		ballVel = ballVel + GRAVITY;
	}

	private void moveWall() {
		upperWall.move((int)-wallVel, 0);
		lowerWall.move((int)-wallVel, 0);
		if (upperWall.getX() < -BALL_DIAM) {
			remove(upperWall);
			remove(lowerWall);
			createNewWall();
			wallCounter++;
			wallVel += 0.3;
		}
	}

	//public void keyTyped(KeyEvent e) {
	public void mousePressed(int x, int y) {
		ballVel = BALL_ACCEL;
	}

	private void checkForCollision() {
		checkForCollisionWithFloor();
		checkForCollisionWithWall();
	}

	private void checkForCollisionWithWall() {
		GObject obj = getElementAt(ball.getX() + BALL_DIAM, ball.getY());
		if ((obj == lowerWall) || (obj == upperWall)) {
			alive = false;
		}
	}

	private void checkForCollisionWithFloor() {
		if (ball.getY() >= getHeight() - BALL_DIAM - BALL_OFFSET) {
			ballVel = 0.0;
			ball.setLocation((int)getWidth()/2, getHeight() - BALL_DIAM - BALL_OFFSET);
		}
	}

	/** Create and place ball. */
	private void setup() {

		ball = new GOval(BALL_DIAM, BALL_DIAM);
		ball.setFilled(true);
		ball.setFillColor(Color.RED);
		add(ball, getWidth()/2, 100);

		createNewWall();

		//addKeyListeners();
	}

	private void createNewWall() {
		int middle = rgen.nextInt(2 * BALL_DIAM, getHeight() - 2 * BALL_DIAM);
		upperWall = new GRect(getWidth(), 0, WALL_WIDTH, middle - BALL_DIAM);
		upperWall.setFilled(true);
		upperWall.setFillColor(rgen.nextColor());
		add(upperWall);
		lowerWall = new GRect(getWidth(), middle + BALL_DIAM, WALL_WIDTH, getHeight());
		lowerWall.setFilled(true);
		lowerWall.setFillColor(rgen.nextColor());
		add(lowerWall);
	}
}
