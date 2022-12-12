package acm_graphics;

import java.util.StringTokenizer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ConsoleProgram: should implement the following methods: <br/>
 * - println() <br/>
 * - readInt() <br/>
 * - readLine() <br/>
 * 
 * Note: this class is not tested very well!
 * 
 * @author ralph
 */
public abstract class ConsoleProgram extends Activity implements Runnable {

	private EditText et;
	private boolean waitingForInput = false;
	private String readResult = null;
	private int readPosition = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(getLocalClassName(), "onCreate()");
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll.setOrientation(LinearLayout.VERTICAL);

		et = new EditText(this);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		et.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
		et.setTextSize(16);
		et.setGravity(Gravity.BOTTOM | Gravity.LEFT);
		// et.setInputType(InputType.TYPE_CLASS_TEXT);
		et.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et.setRawInputType(InputType.TYPE_CLASS_TEXT);
		et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// readResult = v.getText().toString();
				// Log.i("ConsoleProgram", "readResult="+readResult);
				readResult = getWordEntered(v.getText().toString());
				Log.i("ConsoleProgram", "readResult=" + readResult);
				waitingForInput = false;
				return true;
			}
		});
		ll.addView(et);

		setContentView(ll);

		new Thread(this).start();
	}

	@Override
	public void run() {
		// does nothing, needs to be overridden
		while (true) {
			pause(50);
		}
	}

	protected void println(final String msg) {
		setText(msg);
		pause(100);
	}

	private void setText(final String msg) {
		Spanned coloredMsg = Html.fromHtml("<br/>" + msg + "");
		setText(coloredMsg);
	}

	private void setText(final Spanned msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				et.append(msg);
			}
		});
	}

	protected int readInt(String msg) {
		String number = readLine(msg);
		return Integer.parseInt(number);
	}

	protected String readLine(String msg) {
		// String total = "";// et.getText().toString();
		// Log.i("ConsoleProgram", "total=" + total);
		Spanned coloredMsg = Html.fromHtml("<br/><font color='#32CD32'>" + msg + "</font>");
		// et.setText(coloredMsg);
		setText(coloredMsg);
		readPosition = msg.length();
		waitingForInput = true;
		while (waitingForInput) {
			pause(100);
		}
		return readResult;
	}

	private String getWordEntered(String total) {
		// find last line:
		String lastLine = "";
		StringTokenizer st = new StringTokenizer(total, "\n");
		while (st.hasMoreTokens()) {
			lastLine = st.nextToken();
		}
		return lastLine.substring(readPosition);
	}

	protected int getWidth() {
		return et.getWidth();
	}

	protected int getHeight() {
		return et.getHeight();
	}

	protected void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
