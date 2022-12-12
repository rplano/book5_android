package variationenzumthema_ch2;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import variationenzumthema.ch2.R;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * CalculatorActivity
 *
 * In the second semester we learned how to evaluate mathematical expressions in
 * infix notation, e.g.,
 * 
 * ( 2 + ( ( 3 + 4 ) * ( 5 * 6 ) ) ) = 212
 * 
 * using the stack data structure. We now turn this into an activity. It is also
 * a demonstration of the GridLayout.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CalculatorActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "CalculatorActivity";

	private final int NR_OF_COLUMNS = 4;

	private int width = -1;
	private int height = -1;

    private String[] btnLabels = {
    		"7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "(","0",")","+",
            "C","DEL",".","="};

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator_activity);

		getScreenDimensions();

		tv = (TextView) findViewById(R.id.textView2);

		GridLayout gl = (GridLayout) findViewById(R.id.gridLayout1);
		gl.removeAllViews();
		gl.setColumnCount(NR_OF_COLUMNS);
		gl.setRowCount(5);

		Button[] btns = new Button[btnLabels.length];
		for (int i = 0; i < btnLabels.length; i++) {
			btns[i] = new Button(this);
			btns[i].setText(btnLabels[i]);
			btns[i].setLayoutParams(getLayoutParams(width, i));
			btns[i].setOnClickListener(this);
			gl.addView(btns[i]);
		}
	}

	private void getScreenDimensions() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	}

	private GridLayout.LayoutParams getLayoutParams(int width, int i) {
		GridLayout.LayoutParams param = new GridLayout.LayoutParams();
		// param.height = LayoutParams.WRAP_CONTENT;
		// param.width = LayoutParams.WRAP_CONTENT;
		// param.rightMargin = 5;
		// param.topMargin = 5;
		param.width = width / NR_OF_COLUMNS;
		param.setGravity(Gravity.CENTER);
		param.columnSpec = GridLayout.spec(i % NR_OF_COLUMNS);
		param.rowSpec = GridLayout.spec(i / NR_OF_COLUMNS);
		return param;
	}

	@Override
	public void onClick(View v) {
		String lbl = ((Button) v).getText().toString();
		if ("0123456789()+-*/".contains(lbl)) {
			tv.append(lbl);
		} else if (lbl.equals("C")) {
			tv.setText("");
		} else if (lbl.equals("=")) {
			String infix = tv.getText().toString();
			String postfix = convertFromInfixToPostfix(infix);
			Log.i(TAG, postfix);
			int result = evaluate(postfix);
			tv.setText("" + result);
		} else if (lbl.equals("DEL")) {
			String txt = tv.getText().toString();
			tv.setText(txt.substring(0, txt.length() - 1));
		}
	}

	/**
	 * Converting from infix to postfix notation. We read in the tokens one at a
	 * time. If it is an operator, we push it on the stack; if it is an integer,
	 * we print it out; if it is a right parentheses, we pop the topmost element
	 * from the stack and print it out; if it is a left parentheses, we ignore
	 * it.
	 * 
	 * @param infix
	 * @return
	 */
	private String convertFromInfixToPostfix(String infix) {
		Stack<String> stck = new Stack<String>();
		String postfix = "";
		// your code goes here:
		StringTokenizer st = new StringTokenizer(infix, " ()+-*/", true);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if ("+-*/".contains(tok)) {
				stck.push(tok);
			} else if (")".equals(tok)) {
				postfix += stck.pop() + " ";
			} else if ("(".equals(tok)) {
			} else if (" ".equals(tok)) {
			} else {
				postfix += tok + " ";
			}
		}
		return postfix;
	}

	/**
	 * Parse and evaluate a postfix expression. We read the tokens in one at a
	 * time. If it is an integer, push it on the stack; if it is a binary
	 * operator, pop the top two elements from the stack, apply the operator to
	 * the two elements, and push the result back on the stack.
	 * 
	 * @param postfix
	 * @return
	 */
	private int evaluate(String postfix) {
		Stack<Integer> stck = new Stack<Integer>();
		// your code goes here:
		StringTokenizer st = new StringTokenizer(postfix);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if ("+-*/".contains(tok)) {
				int a = stck.pop();
				int b = stck.pop();
				int c = 0;
				if (tok.equals("+")) {
					c = a + b;
				} else if (tok.equals("-")) {
					c = a - b;
				} else if (tok.equals("*")) {
					c = a * b;
				} else if (tok.equals("/")) {
					c = a / b;
				}
				stck.push(c);
			} else {
				int c = Integer.parseInt(tok.trim());
				stck.push(c);
			}
		}
		return stck.pop();
	}
}
