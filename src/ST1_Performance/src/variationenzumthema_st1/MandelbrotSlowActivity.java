package variationenzumthema_st1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * MandelbrotSlowActivity
 * 
 * A GraphicsProgram that draws the Mandelbrot set. Uses standard Android
 * drawing methods.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MandelbrotSlowActivity extends Activity {

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

		// needed for Mandelbrot
		private final int MAX_ITERATION = 50;
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
			mCanvasWidth = w;
			mCanvasHeight = h;
		}

		public void onDraw(Canvas canvas) {
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.WHITE);

			double xStep = (xMax - xMin) / mCanvasWidth * 4;
			double yStep = (yMax - yMin) / mCanvasHeight * 4;
			for (double x = xMin; x < xMax; x += xStep) {
				int i = (int) (((x - xMin) * mCanvasWidth) / (xMax - xMin));
				for (double y = yMin; y < yMax; y += yStep) {
					int j = (int) (((y - yMin) * mCanvasHeight) / (yMax - yMin));
					paint.setColor(function(x, y));
					// c.drawPoint(i, j, paint);
					canvas.drawRect(i, j, i + 4, j + 4, paint);
				}
			}

			// display frame rate
			long endTime = System.currentTimeMillis();
			long fps = 1000 / (endTime - startTime);
			startTime = endTime;
			canvas.drawRect(0, 0, 200, 70, backgroundForTextPaint);
			canvas.drawText("fps:" + fps, 20, 50, textPaint);

			invalidate();
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
	}
}
