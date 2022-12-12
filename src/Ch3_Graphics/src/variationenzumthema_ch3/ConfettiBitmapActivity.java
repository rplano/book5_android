package variationenzumthema_ch3;

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
 * ConfettiBitmapActivity
 *
 * This activity shows how to implement the confetti example using a bitmap.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ConfettiBitmapActivity extends Activity {
	private final int SIZE = 40;
	private final int DELAY = 40;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View gv = new ConfettiBitmapView(this);
		gv.setBackgroundColor(Color.WHITE);
		setContentView(gv);
	}

	class ConfettiBitmapView extends View {
		private Bitmap bitmap;
		private Paint cPaint;
		private Random rgen = new Random();

		public ConfettiBitmapView(Context context) {
			super(context);

			cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			cPaint.setStyle(Paint.Style.FILL);
		}

		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// create randomly sized rect
			int w = SIZE / 2 + rgen.nextInt(SIZE);
			int x = -SIZE + rgen.nextInt(getWidth());
			int y = -SIZE + rgen.nextInt(getHeight());

			cPaint.setColor(rgen.nextInt());

			Canvas can = new Canvas(bitmap);
			can.drawOval(new RectF(x, y, x + w, y + w), cPaint);

			canvas.drawBitmap(bitmap, 0, 0, null);

			pause(DELAY);
			invalidate();
		}

		private void pause(int time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
			}
		}
	}

}
