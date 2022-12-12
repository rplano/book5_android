package variationenzumthema_ch3;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ConfettiActivity
 *
 * This activity shows how to implement the confetti example using simple
 * drawing methods.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ConfettiActivity extends Activity {
	private final int SIZE = 40;
	private final int DELAY = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View gv = new ConfettiView(this);
		gv.setBackgroundColor(Color.WHITE);
		setContentView(gv);
	}

	class ConfettiView extends View {
		private Paint paint;
		private Random rgen = new Random();

		public ConfettiView(Context context) {
			super(context);

			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// create randomly sized oval
			int w = SIZE / 2 + rgen.nextInt(SIZE);
			int x = -SIZE + rgen.nextInt(getWidth());
			int y = -SIZE + rgen.nextInt(getHeight());

			paint.setColor(rgen.nextInt());
			canvas.drawOval(new RectF(x, y, x + w, y + w), paint);

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
