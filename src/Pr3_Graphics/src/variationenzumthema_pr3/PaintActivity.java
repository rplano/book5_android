package variationenzumthema_pr3;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PaintActivity
 *
 * This activity implements a simple finger drawing app. when rotating the
 * device you will notice that it redraws.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PaintActivity extends Activity {

	private ArrayList<Point> points = new ArrayList<Point>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PaintView paintView = new PaintView(this);
		setContentView(paintView);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("PaintActivity", "onSaveInstanceState()");
		outState.putSerializable("points", points);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("PaintActivity", "onRestoreInstanceState()");
		if (savedInstanceState != null) {
			Object obj = savedInstanceState.getSerializable("points");
			if (obj != null) {
				points = (ArrayList<Point>) obj;
			}
		}
	}

	private class PaintView extends View {

		public PaintView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			setBackgroundColor(Color.WHITE);
			Paint color = new Paint();
			color.setStrokeWidth(4);
			color.setColor(Color.BLACK);

			if (points.size() > 1) {
				Point p0 = points.get(0);
				for (int i = 1; i < points.size(); i++) {
					Point p1 = points.get(i);
					canvas.drawLine(p0.x, p0.y, p1.x, p1.y, color);
					p0 = p1;
				}
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int x = (int) event.getX();
			int y = (int) event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				points.add(new Point(x, y));
				invalidate();
				break;
			}
			return true;
		}
	}
}