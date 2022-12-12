package variationenzumthema_pr4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PaintActivity
 *
 * This activity implements a simple finger drawing app. It serializes state to
 * internal storage.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PaintingActivity extends Activity {

	private final String FILE_NAME = "points.ser";

	private List<Point> points = new ArrayList<Point>();
	private PaintView paintView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		paintView = new PaintView(this);
		setContentView(paintView);
	}

	@Override
	protected void onPause() {
		try {
			if (points != null) {
				FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(points);
				oos.close();
				fos.close();
			}
		} catch (Exception e) {
			Log.e(getLocalClassName(), "onPause()" + e);
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			points = (List<Point>) obj;
			ois.close();
			fis.close();
		} catch (Exception e) {
			Log.e(getLocalClassName(), "onResume()" + e);
			points = new ArrayList<Point>();
		}
		paintView.postInvalidate();

		super.onResume();
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
			case MotionEvent.ACTION_POINTER_2_DOWN:
				points = new ArrayList<Point>();
				invalidate();
				break;
			}
			return true;
		}
	}
}

/**
 * Serializable may not be local classes!
 */
class Point implements Serializable {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

}