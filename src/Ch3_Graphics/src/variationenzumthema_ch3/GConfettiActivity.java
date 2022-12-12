package variationenzumthema_ch3;

import java.util.ArrayList;
import java.util.List;
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
 * GConfettiActivity
 *
 * This activity shows how to implement the confetti example using a class
 * inspired by ACM's GOval.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GConfettiActivity extends Activity {
	private final int SIZE = 40;
	private final int DELAY = 40;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View gv = new GConfettiView(this);
		gv.setBackgroundColor(Color.WHITE);
		setContentView(gv);
	}

	class GConfettiView extends View {
		private Random rgen = new Random();
		private List<GGOval> confettis = new ArrayList<GGOval>();

		public GConfettiView(Context context) {
			super(context);
		}

		public void addNewConfetti() {
			// create randomly sized oval
			int width = SIZE / 2 + rgen.nextInt(SIZE);
			int x = -SIZE + rgen.nextInt(getWidth());
			int y = -SIZE + rgen.nextInt(getHeight());
			GGOval oval = new GGOval(x, y, width, width);

			// assign random color
			oval.setColor(rgen.nextInt());

			// add to list
			confettis.add(oval);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			setBackgroundColor(Color.WHITE);

			addNewConfetti();

			// draw all objects
			for (GGOval ov : confettis) {
				ov.draw(canvas);
			}

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

	class GGOval {
		private int x, y, w, h;
		private Paint paint;

		public GGOval(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			this.paint.setStyle(Paint.Style.FILL);
		}

		public void setColor(int color) {
			paint.setColor(color);
		}

		public void draw(Canvas canvas) {
			canvas.drawOval(new RectF(this.x, this.y, this.x + this.w, this.y + this.h), paint);
		}
	}
}
