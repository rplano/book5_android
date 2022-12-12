package variationenzumthema_pr3;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * MondrianActivity
 * 
 * To create Mondrians follow this procedure:
 * 
 * 1) either split canvas horizontally, vertically or do nothing <br/>
 * 2) repeat step 1 with the smaller canvases, until canvases are to small.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MondrianActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MondrianView gv = new MondrianView(this);
		setContentView(gv);

		Toast.makeText(this, "Touch screen to start.", Toast.LENGTH_SHORT).show();
	}

	class MondrianView extends View {
		private static final int MIN_SIZE = 60;
		private Random rgen = new Random();
		private Paint cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		public MondrianView(Context context) {
			super(context);
			
			this.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View vw, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						invalidate();
						break;
					}
					return true;
				}
			});
		}

		public void onDraw(Canvas c) {
			cPaint.setColor(Color.WHITE);
			cPaint.setStyle(Paint.Style.FILL);
			c.drawRect(0, 0, getWidth(), getHeight(), cPaint);
			
			drawMondrian(c, 0, 0, getWidth(), getHeight());
		}

		private void drawMondrian(Canvas c, int i, int j, int width, int height) {
			// base case
			if ((width < MIN_SIZE) || (height < MIN_SIZE)) {
				return;
			}

			// recursive case
			int choice = rgen.nextInt(3);
			switch (choice) {
			case 0: // divide canvas horizontally
				drawMondrian(c, i, j, width / 2, height);
				drawMondrian(c, i + width / 2, j, width / 2, height);
				break;
			case 1: // divide canvas vertically
				drawMondrian(c, i, j, width, height / 2);
				drawMondrian(c, i, j + height / 2, width, height / 2);
				break;
			default: // do nothing
				drawRectangle(c, i, j, width, height);
				break;
			}
		}

		private void drawRectangle(Canvas c, int i, int j, int w, int h) {
			cPaint.setColor(getRandomColor());
			cPaint.setStyle(Paint.Style.FILL);
			c.drawRect(i, j, i + w, j + h, cPaint);
			
			cPaint.setColor(Color.BLACK);
			cPaint.setStrokeWidth(4);
			cPaint.setStyle(Paint.Style.STROKE);
			c.drawRect(i, j, i + w, j + h, cPaint);
		}

		private int getRandomColor() {
			int choice = rgen.nextInt(4);
			switch (choice) {
			case 0:
				return Color.BLUE;
			case 1:
				return Color.RED;
			case 2:
				return Color.YELLOW;
			default:
				return Color.WHITE;
			}
		}
	}

}
