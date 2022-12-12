package variationenzumthema_pr3;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * GameOfLifeActivity
 * 
 * Conway's Game of Life en.wikipedia.org/wiki/Conway's_Game_of_Life
 * 
 * The universe of the Game of Life is an infinite two-dimensional orthogonal
 * grid of square cells, each of which is in one of two possible states, alive
 * or dead. Every cell interacts with its eight neighbours, which are the cells
 * that are horizontally, vertically, or diagonally adjacent.
 * 
 * At each step in time, the following transitions occur: <br/>
 * - Any live cell with fewer than two live neighbours dies, as if caused by
 * under-population. <br/>
 * - Any live cell with two or three live neighbours lives on to the next
 * generation. <br/>
 * - Any live cell with more than three live neighbours dies, as if by
 * overcrowding. <br/>
 * - Any dead cell with exactly three live neighbours becomes a live cell, as if
 * by reproduction. <br/>
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GameOfLifeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GameOfLifeView gv = new GameOfLifeView(this);
		setContentView(gv);
	}

	class GameOfLifeView extends View {
		// needed for drawing
		private int mCanvasWidth;
		private int mCanvasHeight;
		private Bitmap bitmap;
		private int[] bitMapArray;

		// needed for GameOfLife
		private final int CELL_SIZE_IN_PIXEL = 10;
		private final double INITIAL_LIFE_PROBABILITY = 0.5;
		private int SIZE_Y = 100;
		private int SIZE_X = 110;

		private boolean[][] cells;
		private Random rgen;

		// needed for frame rate measurement
		private final int FPS = 15;
		private Paint backgroundForTextPaint;
		private Paint textPaint;
		private long startTime;

		public GameOfLifeView(Context context) {
			super(context);

			// needed for frame rate measurement
			backgroundForTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			backgroundForTextPaint.setColor(Color.WHITE);

			textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			textPaint.setColor(Color.BLACK);
			textPaint.setStrokeWidth(1);
			textPaint.setTextSize(48f);

			startTime = System.currentTimeMillis();
		}

		private void initCells() {
			// setSize(SIZE * CELL_SIZE_IN_PIXEL, SIZE * CELL_SIZE_IN_PIXEL);
			rgen = new Random();
			for (int i = 0; i < SIZE_X * SIZE_Y * INITIAL_LIFE_PROBABILITY; i++) {
				cells[rgen.nextInt(SIZE_X )][rgen.nextInt(SIZE_Y )] = true;
			}
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvasWidth = w;
			mCanvasHeight = h;

			SIZE_Y = mCanvasHeight / CELL_SIZE_IN_PIXEL;
			SIZE_X = mCanvasWidth / CELL_SIZE_IN_PIXEL;
			cells = new boolean[SIZE_X][SIZE_Y];
			bitMapArray = new int[SIZE_X * SIZE_Y];

			initCells();
		}

		public void destroy() {
			if (bitmap != null) {
				bitmap.recycle();
			}
		}

		public void onDraw(Canvas c) {

			transitions();

			// displayCells();
			for (int i = 0; i < SIZE_X; i++) {
				for (int j = 0; j < SIZE_Y; j++) {
					if (cells[i][j]) {
						bitMapArray[j * SIZE_X + i] = Color.BLACK;
					} else {
						bitMapArray[j * SIZE_X + i] = Color.WHITE;
					}
				}
			}
			bitmap.setPixels(bitMapArray, 0, SIZE_X, 0, 0, SIZE_X, SIZE_Y);

			// draw bitmap on canvas
			// c.drawBitmap(bitmap, 0, 0, null); // unscaled
			Matrix m = new Matrix();
			m.setScale((float) mCanvasWidth / SIZE_X, (float) mCanvasHeight / SIZE_Y);
			c.drawBitmap(bitmap, m, null);

			// display frame rate
			long endTime, fps;
			while (true) {
				endTime = System.currentTimeMillis();
				fps = 1000 / (endTime - startTime);
				if (fps < FPS)
					break;
				pause(5);
			}
			startTime = endTime;
			c.drawRect(0, 0, 200, 70, backgroundForTextPaint);
			c.drawText("fps:" + fps, 20, 50, textPaint);

			// force a redraw
			invalidate();
		}

		private void pause(int time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
			}
		}

		private void transitions() {
			boolean[][] temp = new boolean[SIZE_X][SIZE_Y];
			for (int i = 0; i < SIZE_X; i++) {
				for (int j = 0; j < SIZE_Y; j++) {
					int nrOfNeighbors = countNeighbors(i, j);
					if (cells[i][j]) {
						// Any live cell with fewer than two live neighbours
						// dies,
						// as if caused by under-population.

						// Any live cell with two or three live neighbours lives
						// on
						// to the next generation.
						if ((nrOfNeighbors == 2) || (nrOfNeighbors == 3)) {
							temp[i][j] = true;
						}
						// Any live cell with more than three live neighbours
						// dies,
						// as if by overcrowding.

					} else {
						// Any dead cell with exactly three live neighbours
						// becomes
						// a live cell, as if by reproduction.
						if (nrOfNeighbors == 3) {
							temp[i][j] = true;
						}
					}
				}
			}
			cells = temp;
		}

		private int countNeighbors(int i, int j) {
			int counter = 0;
			if (testCell(i - 1, j - 1))
				counter++;
			if (testCell(i - 1, j))
				counter++;
			if (testCell(i - 1, j + 1))
				counter++;
			if (testCell(i, j - 1))
				counter++;
			if (testCell(i, j + 1))
				counter++;
			if (testCell(i + 1, j - 1))
				counter++;
			if (testCell(i + 1, j))
				counter++;
			if (testCell(i + 1, j + 1))
				counter++;
			return counter;
		}

		// truncates at borders
		private boolean testCell1(int i, int j) {
			if (i >= 0 && i <= SIZE_X - 1) {
				if (j >= 0 && j <= SIZE_Y - 1) {
					return cells[i][j];
				}
			}
			return false;
		}

		// loops around at borders
		private boolean testCell(int i, int j) {
			return cells[(i + SIZE_X) % SIZE_X][(j + SIZE_Y) % SIZE_Y];
		}
	}
}
