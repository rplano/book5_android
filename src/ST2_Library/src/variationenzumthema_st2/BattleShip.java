package variationenzumthema_st2;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Asteroids: BattleShip
 * 
 * A simple version of the BattleShip game.
 * 
 * @see https://en.wikipedia.org/wiki/Battleship_(game)
 * @see http://www.learn4good.com/games/board/battleship.htm computer puts ships
 *      at random positions AircraftCarrier 5 Battleship 4 Submarine 3 Destroyer
 *      3 PatrolBoat 2
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BattleShip extends GraphicsProgram {
	// private final int SIZE = 300;
	// private final int OFFSET = 46;
	private final int FONT_SIZE = 64;
	private final int BOARD_SIZE = 10;
	private int STEP;

	// AircraftCarrier 5, Battleship 4, Submarine 3, Destroyer 3, PatrolBoat 2
	// the order is important, because we want to first place the large ships
	private final int[] SHIP_SIZES = { 5, 4, 3, 3, 2 };

	private RandomGenerator rgen = new RandomGenerator();

	private int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

	public void run() {
		waitForTouch();
		setup();
	}

	@Override
	public void mousePressed(int i, int j) {
		i = i / STEP;
		j = j / STEP;
		if ((i < BOARD_SIZE) && (j < BOARD_SIZE)) {
			showLabelAt(i, j);
		}
	}

	private void setup() {
		STEP = Math.min(getWidth(), getHeight()) / BOARD_SIZE;
		drawLines();
		initBoard();
		// drawBoard();
		addMouseListeners();
	}

	private void drawBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < board.length; j++) {
				showLabelAt(i, j);
			}
		}
	}

	private void showLabelAt(int i, int j) {
		GLabel lbl = new GLabel("" + board[i][j]);
		if (board[i][j] == 0) {
			lbl = new GLabel(".");
		}
		lbl.setFont("SansSerif-bold-" + FONT_SIZE);
		int x = i * STEP + 40;
		int y = j * STEP + 80;
		add(lbl, x, y);
	}

	private void initBoard() {
		for (int j = 0; j < SHIP_SIZES.length; j++) {
			placeShip(j + 1, SHIP_SIZES[j]);
		}
		// // random
		// for (int k = 0; k < 20; k++) {
		// int i = rgen.nextInt(0, BOARD_SIZE - 1);
		// int j = rgen.nextInt(0, BOARD_SIZE - 1);
		// board[i][j] = rgen.nextInt(0, 5);
		// }
	}

	// the order is important, because we want to first place the large ships
	private void placeShip(int shipNr, int shipSize) {
		boolean locationOK = false;
		while (!locationOK) {
			int i = rgen.nextInt(0, BOARD_SIZE - shipSize - 1);
			int j = rgen.nextInt(0, BOARD_SIZE - shipSize - 1);
			boolean directionRight = rgen.nextBoolean();
			if (directionRight) {
				boolean isOK = true;
				for (int k = 0; k < shipSize; k++) {
					if (board[i][j + k] > 0) {
						isOK = false;
					}
				}
				if (isOK) {
					for (int k = 0; k < shipSize; k++) {
						board[i][j + k] = shipSize;
					}
					locationOK = true;
				}
			} else {
				boolean isOK = true;
				for (int k = 0; k < shipSize; k++) {
					if (board[i + k][j] > 0) {
						isOK = false;
					}
				}
				if (isOK) {
					for (int k = 0; k < shipSize; k++) {
						board[i + k][j] = shipSize;
					}
					locationOK = true;
				}

			}
		}
	}

	private void drawLines() {
		int x = STEP;
		for (int i = 0; i < BOARD_SIZE; i++) {
			GLine verticalLine = new GLine(x, 0, x, STEP * BOARD_SIZE);
			add(verticalLine);
			GLine horizontalLine = new GLine(0, x, STEP * BOARD_SIZE, x);
			add(horizontalLine);
			x += STEP;
		}
	}
}
