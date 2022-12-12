package variationenzumthema_pr1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NumpadActivity
 * 
 * Demonstrates how to use an Intent to send and receive data from another activity.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NumpadActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "NumpadActivity";

	private final int NR_OF_COLUMNS = 3;
	private final int FONT_SIZE = 24;

	private int btnId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		btnId = getIntent().getIntExtra("id", -1);
		Log.i(TAG, "id=" + btnId);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		GridLayout gl = new GridLayout(this);
		gl.removeAllViews();
		gl.setColumnCount(NR_OF_COLUMNS);
		gl.setRowCount(NR_OF_COLUMNS);

		Button[] btns = new Button[NR_OF_COLUMNS * NR_OF_COLUMNS];
		for (int id = 0; id < btns.length; id++) {
			btns[id] = new Button(this);
			btns[id].setId(id + 1);
			btns[id].setTextSize(FONT_SIZE);
			btns[id].setText("" + (id + 1));
			btns[id].setOnClickListener(this);
			gl.addView(btns[id]);
		}

		ll.addView(gl);
		setContentView(ll);
	}

	@Override
	public void onClick(View v) {
		int num = ((Button) v).getId();
		Intent intent = new Intent();
		intent.putExtra("id", btnId);
		intent.putExtra("num", num);
		setResult(RESULT_OK, intent);
		finish();
	}
}