package variationenzumthema_st1;

import java.nio.IntBuffer;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * FastGRectActivity
 * 
 * A lesson on graphics performance.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FastGRectActivity extends Activity {
	private final int SIZE = 40;
	private final int DELAY = 40;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View gv = new FastGRectView(this);
		setContentView(gv);
	}

	/**
	 * Time: 490ms, uses standard bitmap to draw, but using GRect. Seperated
	 * creation from drawing.
	 */
	class FastGRectView extends View {
		private Paint paint;
		private Random rgen = new Random();
		private int mCanvasWidth;
		private int mCanvasHeight;
		private Bitmap bitmap;
		private int[] bitMapArray;

		public FastGRectView(Context context) {
			super(context);

			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(1);
			paint.setTextSize(48f);
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvasWidth = w;
			mCanvasHeight = h;
			bitMapArray = new int[mCanvasWidth * mCanvasHeight];
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// seperate creation (this part takes about 1250ms)
			GRect[] rects = new GRect[50000];
			for (int k = 0; k < 50000; k++) {
				// create randomly sized rect
				int w = rgen.nextInt(SIZE);
				int x = rgen.nextInt(getWidth());
				int y = rgen.nextInt(getHeight());

				rects[k] = new GRect(x, y, w, w);
				rects[k].setColor(rgen.nextInt());
			}

			// from drawing (this part also takes about 1250ms)
			long startTime = System.currentTimeMillis();
			// bitMapArray = new int[mCanvasWidth * mCanvasHeight];
			intfill(bitMapArray, 0);
			for (int k = 0; k < 50000; k++) {
				rects[k].draw(bitMapArray);
			}
			// this call takes 42ms:
			// bitmap.setPixels(bitMapArray, 0, mCanvasWidth, 0, 0,
			// mCanvasWidth, mCanvasHeight);
			// this call takes 9ms and does the same thing:
			bitmap.copyPixelsFromBuffer(IntBuffer.wrap(bitMapArray));

			// draw bitmap on canvas
			canvas.drawBitmap(bitmap, 0, 0, null);

			long time = System.currentTimeMillis() - startTime;
			paint.setColor(Color.WHITE);
			canvas.drawRect(0, 0, 250, 70, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText("time:" + time, 20, 50, paint);
		}

		private class GRect {
			protected int x, y, w, h;
			private int color;

			public GRect(int x, int y, int w, int h) {
				this.x = x;
				this.y = y;
				this.w = w;
				this.h = h;
				this.color = Color.BLUE;
			}

			public void setColor(int color) {
				this.color = color;
			}

			public void draw(int[] bitMapArray) {
				int len = bitMapArray.length;
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						int idx = (y + j) * mCanvasWidth + (x + i);
						if (idx < len) {
							bitMapArray[(y + j) * mCanvasWidth + (x + i)] = color;
						}
					}
				}
			}
		}
	}

	/**
	 * Time: 740ms, uses standard bitmap to draw.
	 */
	class FastGRectView3 extends View {
		private Paint paint;
		private Random rgen = new Random();
		private int mCanvasWidth;
		private int mCanvasHeight;
		private Bitmap bitmap;
		private int[] bitMapArray;

		public FastGRectView3(Context context) {
			super(context);

			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(1);
			paint.setTextSize(48f);
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvasWidth = w;
			mCanvasHeight = h;
			bitMapArray = new int[mCanvasWidth * mCanvasHeight];
		}

		@Override
		protected void onDraw(Canvas canvas) {
			long startTime = System.currentTimeMillis();

			for (int k = 0; k < 50000; k++) {
				// create randomly sized rect
				int w = rgen.nextInt(SIZE);
				int x = rgen.nextInt(getWidth() - SIZE);
				int y = rgen.nextInt(getHeight() - SIZE);

				// drawRect:
				fillRect(x, y, w, w, rgen.nextInt());
				// drawRect(x, y, w, w, rgen.nextInt());
			}
			bitmap.setPixels(bitMapArray, 0, mCanvasWidth, 0, 0, mCanvasWidth, mCanvasHeight);

			// draw bitmap on canvas
			canvas.drawBitmap(bitmap, 0, 0, null);

			long time = System.currentTimeMillis() - startTime;
			paint.setColor(Color.WHITE);
			canvas.drawRect(0, 0, 250, 70, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText("time:" + time, 20, 50, paint);
		}

		private void fillRect(int x, int y, int w, int h, int color) {
			int len = bitMapArray.length;
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int idx = (y + j) * mCanvasWidth + (x + i);
					if (idx < len) {
						bitMapArray[(y + j) * mCanvasWidth + (x + i)] = color;
					}
				}
			}
		}

		private void drawRect(int x, int y, int w, int h, int color) {
			int len = bitMapArray.length;
			for (int i = 0; i < w; i++) {
				int j = 0;
				int idx = (y + j) * mCanvasWidth + (x + i);
				if (idx < len) {
					bitMapArray[(y + j) * mCanvasWidth + (x + i)] = color;
				}
				j = h - 1;
				idx = (y + j) * mCanvasWidth + (x + i);
				if (idx < len) {
					bitMapArray[(y + j) * mCanvasWidth + (x + i)] = color;
				}
			}
			for (int j = 0; j < h; j++) {
				int i = 0;
				int idx = (y + j) * mCanvasWidth + (x + i);
				if (idx < len) {
					bitMapArray[(y + j) * mCanvasWidth + (x + i)] = color;
				}
				i = w - 1;
				idx = (y + j) * mCanvasWidth + (x + i);
				if (idx < len) {
					bitMapArray[(y + j) * mCanvasWidth + (x + i)] = color;
				}
			}
		}
	}

	/**
	 * Time: 1260ms, uses standard canvas draw functions, but using GRect.
	 * Seperated creation from drawing.
	 */
	class FastGRectView2 extends View {
		private Paint paint;
		private Random rgen = new Random();

		public FastGRectView2(Context context) {
			super(context);

			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(1);
			paint.setTextSize(48f);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// seperate creation (this part takes about 1250ms)
			GRect[] rects = new GRect[50000];
			for (int k = 0; k < 50000; k++) {
				// create randomly sized rect
				int w = rgen.nextInt(SIZE);
				int x = rgen.nextInt(getWidth());
				int y = rgen.nextInt(getHeight());

				rects[k] = new GRect(x, y, w, w);
				rects[k].setColor(rgen.nextInt());
			}

			// from drawing (this part also takes about 1250ms)
			long startTime = System.currentTimeMillis();
			for (int k = 0; k < 50000; k++) {
				rects[k].draw(canvas);
			}

			long time = System.currentTimeMillis() - startTime;
			paint.setColor(Color.WHITE);
			canvas.drawRect(0, 0, 250, 70, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText("time:" + time, 20, 50, paint);
		}

		private class GRect {
			protected int x, y, w, h;
			private Paint paint;

			public GRect(int x, int y, int w, int h) {
				this.x = x;
				this.y = y;
				this.w = w;
				this.h = h;
				this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setColor(Color.BLUE);
				paint.setStrokeWidth(1);
				paint.setStyle(Paint.Style.FILL);
			}

			public void setColor(int color) {
				paint.setColor(color);
			}

			public void draw(Canvas canvas) {
				canvas.drawRect(new RectF(this.x, this.y, this.x + this.w, this.y + this.h), paint);
			}
		}
	}

	/**
	 * Time: 1570ms, uses standard canvas draw functions.
	 */
	class FastGRectView1 extends View {
		private Paint paint;
		private Random rgen = new Random();

		public FastGRectView1(Context context) {
			super(context);

			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(1);
			paint.setTextSize(48f);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			long startTime = System.currentTimeMillis();

			for (int k = 0; k < 50000; k++) {
				int w = rgen.nextInt(SIZE);
				int x = rgen.nextInt(getWidth());
				int y = rgen.nextInt(getHeight());

				paint.setColor(rgen.nextInt());
				canvas.drawRect(new RectF(x, y, x + w, y + w), paint);
			}

			long time = System.currentTimeMillis() - startTime;
			paint.setColor(Color.WHITE);
			canvas.drawRect(0, 0, 250, 70, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText("time:" + time, 20, 50, paint);
		}
	}

	public static void intfill(int[] array, int value) {
		int len = array.length;
		if (len > 0) {
			array[0] = value;
			for (int i = 1; i < len; i += i) {
				System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
			}
		}
	}
}
