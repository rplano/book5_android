package variationenzumthema_ch3;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TouchActivity
 *
 * This activity shows how to detect touch events.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TouchActivity extends Activity {

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tv = new TextView(this);
		tv.setTextSize(16);
		setContentView(tv);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case (MotionEvent.ACTION_CANCEL):
			appendToTextView("ACTION_CANCEL");
			return true;
		case (MotionEvent.ACTION_DOWN):
			appendToTextView("ACTION_DOWN");
			return true;
		case (MotionEvent.ACTION_MOVE):
			appendToTextView("ACTION_MOVE");
			return true;
		case (MotionEvent.ACTION_OUTSIDE):
			appendToTextView("ACTION_OUTSIDE");
			return true;
		case (MotionEvent.ACTION_SCROLL):
			appendToTextView("ACTION_SCROLL");
			return true;
		case (MotionEvent.ACTION_UP):
			appendToTextView("ACTION_UP");
			return true;
		case (MotionEvent.ACTION_POINTER_2_DOWN):
			appendToTextView("ACTION_POINTER_2_DOWN");
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		appendToTextView("onKeyDown(): keyCode=" + keyCode);
		return super.onKeyDown(keyCode, event);
	}

	private void appendToTextView(String text) {
		String content = tv.getText().toString();
		content += text + "\n";
		tv.setText(content);
	}
}