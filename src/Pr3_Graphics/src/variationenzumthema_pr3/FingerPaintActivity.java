package variationenzumthema_pr3;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.view.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * FingerPaintActivity
 *
 * This activity implements a simple finger drawing app using the drawPath()
 * method.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FingerPaintActivity extends Activity {

	private final int STROKE_WIDTH = 4;
	private final int PEN_COLOR = 0xFF000000;
	private final int GRID_COLOR = 0x11000000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FingerPaintView myView = new FingerPaintView(this);
		setContentView(myView);
	}

	private class FingerPaintView extends View {

		private Paint paint;
		private Path path;

		// private List<Path> allPaths;

		private float curX, curY;
		private static final float TOUCH_TOLERANCE = 4;

		public FingerPaintView(Context c) {
			super(c);
			init();
		}

		private void init() {
			path = new Path();
			// allPaths = new ArrayList<Path>();

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setColor(PEN_COLOR);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			drawGrid(canvas);

			// draw old paths
			// for (Path pnp : allPaths) {
			// canvas.drawPath(pnp, paint);
			// }

			// draw current path
			canvas.drawPath(path, paint);
		}

		private void drawGrid(Canvas canvas) {
			Paint gridPaint = new Paint();
			gridPaint.setAntiAlias(true);
			gridPaint.setDither(true);
			gridPaint.setColor(GRID_COLOR);
			gridPaint.setStyle(Paint.Style.STROKE);
			gridPaint.setStrokeWidth(1);
			for (int i = 0; i < getWidth(); i += 50) {
				canvas.drawLine(i, 0, i, getHeight(), gridPaint);
			}
			for (int i = 0; i < getHeight(); i += 50) {
				canvas.drawLine(0, i, getWidth(), i, gridPaint);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchDown(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touchMove(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touchUp();
				invalidate();
				break;
			}
			return true;
		}

		private void touchDown(float x, float y) {
			path.reset();
			path.moveTo(x, y);
			curX = x;
			curY = y;
		}

		private void touchMove(float x, float y) {
			float dx = Math.abs(x - curX);
			float dy = Math.abs(y - curY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				path.quadTo(curX, curY, (x + curX) / 2, (y + curY) / 2);
				// path.lineTo(curX, curY);
				curX = x;
				curY = y;
			}
		}

		private void touchUp() {
			path.lineTo(curX, curY);

			// the class Path has a copy-constructor
			// Path copyPath = new Path(path);
			// allPaths.add(copyPath);

			// path.reset();
		}
	}
}
