package variationenzumthema_ch3;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GestureActivity
 *
 * This activity shows how to detect common gestures.
 * 
 * The difference between Scroll and fling
 * 
 * onFling: is that the user lifts his finger in the end of the movement (that
 * is the reason for what onFling is called one time).
 * 
 * onScroll: is the general process of moving the viewport (that is, the
 * 'window' of content you're looking at).
 * 
 * @see https://developer.android.com/training/gestures/detector.html#detect
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GestureActivity extends Activity {

	private TextView tv;
	private GestureDetector mDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tv = new TextView(this);
		tv.setTextSize(16);
		setContentView(tv);

		mDetector = new GestureDetector(this, new MyGestureListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

		// onDown should always return true, because all gestures start with it
		@Override
		public boolean onDown(MotionEvent event) {
			appendToTextView("onDown");
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
			appendToTextView("onFling");
			return true;
		}

		@Override
		public void onLongPress(MotionEvent event) {
			appendToTextView("onLongPress");
		}

		@Override
		public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
			appendToTextView("onScroll");
			return true;
		}

		@Override
		public void onShowPress(MotionEvent event) {
			appendToTextView("onShowPress");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			appendToTextView("onSingleTapUp");
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			appendToTextView("onDoubleTap");
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent event) {
			appendToTextView("onDoubleTapEvent");
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			appendToTextView("onSingleTapConfirmed");
			return true;
		}
	}

	private void appendToTextView(String text) {
		String content = tv.getText().toString();
		content += text + "\n";
		tv.setText(content);
	}
}