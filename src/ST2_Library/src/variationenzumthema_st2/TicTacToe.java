package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Asteroids: TicTacToe
 * 
 * An implementation of the TicTacToe game.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TicTacToe extends GraphicsProgram {
	private final int OFFSET = 47;
	private int WIDTH = 600;
	private int HEIGHT = 600 + OFFSET;
	private int CELL_WIDTH = 200;
	private double SCALE = 1.0;
	private int currentPlayer = 1;

	public void init() {
	}

	public void run() {
		waitForTouch();
		setup();
	}

	public void mousePressed(int x, int y) {
		int i = x / CELL_WIDTH;
		int j = y / CELL_WIDTH;

		if (TicTacToeLogic.isMoveAllowed(currentPlayer, i, j)) {
			displayPlayer(i, j, currentPlayer);
		}

		if (TicTacToeLogic.isGameOver()) {
			displayGameOver();
		}
	}

	private void displayGameOver() {
		GLabel lbl = new GLabel("Player " + currentPlayer + " lost!");
		lbl.setColor(Color.RED);
		lbl.setFont("SansSerif-72");
		lbl.setLocation((WIDTH - lbl.getWidth()) / 2, HEIGHT / 2);
		add(lbl);
	}

	private void displayPlayer(int i, int j, int player2) {
		if (currentPlayer == 1) {
			GImage img = new GImage("TicTacToe_X.png", this);
			img.scale(SCALE);
			add(img, i * CELL_WIDTH, j * CELL_WIDTH);
			currentPlayer = 2;
		} else {
			GImage img = new GImage("TicTacToe_O.png", this);
			img.scale(SCALE);
			add(img, i * CELL_WIDTH, j * CELL_WIDTH);
			currentPlayer = 1;
		}
	}

	private void setup() {
		// setSize(WIDTH, HEIGHT);
		GImage background = new GImage("TicTacToe_background.png", this);
		double backgroundSize = background.getWidth();
		double screenSize = this.getWidth();
		SCALE = screenSize / backgroundSize;
		WIDTH = (int) (backgroundSize * SCALE);
		HEIGHT = WIDTH;
		CELL_WIDTH = (int) (CELL_WIDTH * SCALE);
		background.scale(SCALE);
		add(background);
		addMouseListeners();
	}
}
