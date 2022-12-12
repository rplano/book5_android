package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * SlidingPuzzle
 * 
 * Play a simple version of the SlidingPuzzle game.
 * 
 * @see 15 puzzle, https://en.wikipedia.org/wiki/15_puzzle
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SlidingPuzzle extends GraphicsProgram {
	private final int NR = 6;
	private int WIDTH;
	private int SIZE;

	private RandomGenerator rgen = new RandomGenerator();
	private int emptyLocation = 0;
	private int[][] nrs;

	public void run() {
		// setSize(WIDTH + 3, WIDTH + 47);
		waitForTouch();

		WIDTH = Math.min(getWidth(), getHeight());
		SIZE = WIDTH / NR;

		createRandomPuzzle();
		drawPuzzle();
		// printScore();

		// addMouseListeners();
	}

	@Override
	// public void mousePressed(MouseEvent e) {
	public void mousePressed(int x, int y) {
		// int x = e.getX();
		// int y = e.getY();
		GObject obj = getElementAt(x, y);
		if (obj != null) {
			int eI = emptyLocation / NR;
			int eJ = emptyLocation % NR;
			int i = x / SIZE;
			int j = y / SIZE;
			int di = i - eI;
			int dj = j - eJ;
			if (((di == 0) && (dj * dj == 1)) || ((dj == 0) && (di * di == 1))) {
				// move stone to empty location
				obj.setLocation(eI * SIZE, eJ * SIZE);
				// remember new empty location
				emptyLocation = i * NR + j;
				// update array
				int tmp = nrs[i][j];
				nrs[i][j] = 0;
				nrs[eI][eJ] = tmp;
				// println("eI="+(eI)+",eJ="+(eJ)+","+nrs[eI][eJ]);
				// printScore();
			}
		}
	}

	/**
	 * avg = 422, min = 308, max = 556 for a 6x6 puzzle
	 */
	private void printScore() {
		int score = 0;
		for (int i = 0; i < NR; i++) {
			for (int j = 0; j < NR; j++) {
				int idealScore = i * NR + j + 1;
				if (i == NR - 1 && j == NR - 1) {
					idealScore = 0;
				}
				// println("i="+i+",j="+j+",is="+idealScore+",nrs="+nrs[j][i]);
				score += Math.abs(nrs[j][i] - idealScore);
			}
		}
		// score -= 36;
		// println(score);
		Toast.makeText(this, "Score: " + score, Toast.LENGTH_SHORT).show();
	}

	private void drawPuzzle() {
		for (int i = 0; i < nrs.length; i++) {
			for (int j = 0; j < nrs.length; j++) {
				if (nrs[i][j] > 0) {
					String space = " ";
					if (nrs[i][j] >= 10) {
						space = "";
					}
					GRectWithLabel lbl = new GRectWithLabel(space + nrs[i][j], SIZE);
					add(lbl, i * SIZE, j * SIZE);
				} else {
					// remember emtpy location
					emptyLocation = i * NR + j;
				}
			}
		}
	}

	private void createRandomPuzzle() {
		// int[][] nrs;
		nrs = new int[NR][NR];
		int counter = 1;
		while (counter < NR * NR) {
			int i = rgen.nextInt(NR);
			int j = rgen.nextInt(NR);
			if (nrs[i][j] == 0) {
				nrs[i][j] = counter;
				counter++;
			}
		}
		// return nrs;
	}

	private class GRectWithLabel extends GCompound {
		private GLabel lbl;
		private int size;

		public GRectWithLabel(String label, int size) {
			super();
			this.size = size;
			this.w = size;
			this.h = size;

			setBackground(Color.WHITE);

			add(new GRect(size, size));

			lbl = new GLabel(label);
			lbl.setFont("Serif-Bold-" + size / 2);
			lbl.setLocation(getX() + size / 8, getY() + 5 * size / 8);
			add(lbl);
		}
	}
}
