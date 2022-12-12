package variationenzumthema_pr6;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

/**
 * int nrCPUs = Runtime.getRuntime().availableProcessors();
 * 
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * MandelbrotSuperFastActivity
 * 
 * A GraphicsProgram that draws the Mandelbrot set. Uses a bitmap to do the
 * drawing and distributes the load among several threads.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MandelbrotSuperFastActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MandelbrotView gv = new MandelbrotView(this);
		setContentView(gv);
	}

	class MandelbrotView extends View {
		// needed for drawing
		private int mCanvasWidth;
		private int mCanvasHeight;
		private Bitmap bitmap;
		
		private final int NR_OF_THREADS = 16;
		private boolean[] isDone = new boolean[NR_OF_THREADS];
		//private int[] bitMapArray;
		private int[][] bitMapArray2;

		// needed for Mandelbrot
		private final int MAX_ITERATION = 100;
		private final int RAINBOW_COLOR_STEP = 10; // 10 * 6
		private final int RAINBOW_NR_OF_COLORS = 10 * 6;
		private int[] RAINBOW_COLORS = new int[RAINBOW_NR_OF_COLORS];
		private double xMin = -2.0;
		private double xMax = 1.0;
		private double yMin = -1.5;
		private double yMax = 1.5;

		// needed for frame rate measurement
		private Paint backgroundForTextPaint;
		private Paint textPaint;
		private long startTime;

		public MandelbrotView(Context context) {
			super(context);
			initColorTable();

			// needed for frame rate measurement
			backgroundForTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			backgroundForTextPaint.setColor(Color.WHITE);

			textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			textPaint.setColor(Color.BLACK);
			textPaint.setStrokeWidth(1);
			textPaint.setTextSize(48f);

			startTime = System.currentTimeMillis();
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvasWidth = w;
			mCanvasHeight = h;
			//bitMapArray = new int[mCanvasWidth * mCanvasHeight];
			bitMapArray2 = new int[NR_OF_THREADS][mCanvasWidth * mCanvasHeight / NR_OF_THREADS];
		}

		public void destroy() {
			if (bitmap != null) {
				bitmap.recycle();
			}
		}

		public void onDraw(Canvas canvas) {

			// int[][] bitMapArray2 = new int[NR_OF_THREADS][mCanvasWidth *
			// mCanvasHeight/NR_OF_THREADS];
			// int[] bitMapArray2a = new int[mCanvasWidth * mCanvasHeight/2];
			// int[] bitMapArray2b = new int[mCanvasWidth * mCanvasHeight/2];
			// ConnectionTask connectionTask1 = new
			// ConnectionTask(0,bitMapArray2a);
			// connectionTask1.execute((double)mCanvasWidth,(double)mCanvasHeight/2,xMin,xMax,yMin
			// ,0.0);
			// ConnectionTask connectionTask1 = new
			// ConnectionTask(0,bitMapArray2[0]);
			// connectionTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(double)mCanvasWidth,(double)mCanvasHeight/2,xMin,xMax,yMin
			// ,0.0);

			// ConnectionTask connectionTask2 = new
			// ConnectionTask(1,bitMapArray2b);
			// connectionTask2.execute((double)mCanvasWidth,(double)mCanvasHeight/2,xMin,xMax,0.0
			// ,yMax);
			// ConnectionTask connectionTask2 = new
			// ConnectionTask(1,bitMapArray2[1]);
			// connectionTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,(double)mCanvasWidth,(double)mCanvasHeight/2,xMin,xMax,0.0
			// ,yMax);

			double dy = (yMax - yMin) / NR_OF_THREADS;
			double y = yMin;
			for (int i = 0; i < NR_OF_THREADS; i++) {
				MandelbrotTask connectionTask1 = new MandelbrotTask(i, bitMapArray2[i]);
				// note execute() will not run tasks on seperate processors,
				// hence use executeOnExecutor()!
				connectionTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (double) mCanvasWidth,
						(double) mCanvasHeight / NR_OF_THREADS, xMin, xMax, y, y + dy);
				y += dy;
			}

			// make sure all threads are done
			boolean done = false;
			while (!done) {
				done = true;
				for (int i = 0; i < isDone.length; i++) {
					if (!isDone[i]) {
						done = false;
					}
				}
				pause(2);
			}

			// glue bitmap out of different stripes
			int dHeight = mCanvasHeight / NR_OF_THREADS;
			int height = 0;
			for (int i = 0; i < NR_OF_THREADS; i++) {
				bitmap.setPixels(bitMapArray2[i], 0, mCanvasWidth, 0, height, mCanvasWidth,
						mCanvasHeight / NR_OF_THREADS);
				height += dHeight;
			}

			// draw bitmap on canvas
			canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
					new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), null);

			// display frame rate
			long endTime = System.currentTimeMillis();
			long fps = 1000 / (endTime - startTime);
			startTime = endTime;
			canvas.drawRect(0, 0, 200, 70, backgroundForTextPaint);
			canvas.drawText("fps:" + fps, 20, 50, textPaint);


			// reset isDone array for next frame
			for (int i = 0; i < isDone.length; i++) {
				isDone[i] = false;
			}
			invalidate();
		}

		private void pause(int time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
			}
		}

		private void initColorTable() {
			int i = 0;
			for (int r = 0; r < RAINBOW_COLOR_STEP; r++) {
				RAINBOW_COLORS[i] = Color.rgb(r * 255 / RAINBOW_COLOR_STEP, 255, 0);
				i++;
			}
			for (int g = RAINBOW_COLOR_STEP; g > 0; g--) {
				RAINBOW_COLORS[i] = Color.rgb(255, g * 255 / RAINBOW_COLOR_STEP, 0);
				i++;
			}
			for (int b = 0; b < RAINBOW_COLOR_STEP; b++) {
				RAINBOW_COLORS[i] = Color.rgb(255, 0, b * 255 / RAINBOW_COLOR_STEP);
				i++;
			}
			for (int r = RAINBOW_COLOR_STEP; r > 0; r--) {
				RAINBOW_COLORS[i] = Color.rgb(r * 255 / RAINBOW_COLOR_STEP, 0, 255);
				i++;
			}
			for (int g = 0; g < RAINBOW_COLOR_STEP; g++) {
				RAINBOW_COLORS[i] = Color.rgb(0, g * 255 / RAINBOW_COLOR_STEP, 255);
				i++;
			}
			for (int b = RAINBOW_COLOR_STEP; b > 0; b--) {
				RAINBOW_COLORS[i] = Color.rgb(0, 255, b * 255 / RAINBOW_COLOR_STEP);
				i++;
			}
			// RAINBOW_COLORS[i] = new Color(0, 255, 0);
		}

		class MandelbrotTask extends AsyncTask<Double, Void, Long> {
			private int myIndex = -1;
			private int[] bitMapArray;

			public MandelbrotTask(int indx, int[] bitMapArray) {
				this.myIndex = indx;
				this.bitMapArray = bitMapArray;
			}

			protected Long doInBackground(Double... params) {
				long startTime = System.currentTimeMillis();

				int mCanvasWidth = (int) params[0].doubleValue();
				int mCanvasHeight = (int) params[1].doubleValue();
				double xMin = params[2];
				double xMax = params[3];
				double yMin = params[4];
				double yMax = params[5];

				// draw pixels in bitmap
				double xStep = (xMax - xMin) / mCanvasWidth * 1;
				double yStep = (yMax - yMin) / mCanvasHeight * 1;
				for (double x = xMin; x < xMax; x += xStep) {
					int i = (int) (((x - xMin) * mCanvasWidth) / (xMax - xMin));
					for (double y = yMin; y < yMax; y += yStep) {
						int j = (int) (((y - yMin) * mCanvasHeight) / (yMax - yMin));
						bitMapArray[j * mCanvasWidth + i] = function(x, y);
					}
				}

				isDone[myIndex] = true;
				return (System.currentTimeMillis() - startTime);
			}

			private int function(double x0, double y0) {
				double x = 0.0;
				double y = 0.0;
				int iteration = 0;
				while (x * x + y * y < 4 && iteration < MAX_ITERATION) {
					double xtemp = x * x - y * y + x0;
					y = 2 * x * y + y0;
					x = xtemp;
					iteration++;
				}
				return RAINBOW_COLORS[iteration % RAINBOW_NR_OF_COLORS];
			}

		}
	}
}
