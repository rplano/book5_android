package variationenzumthema_st4;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.st4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * HashActivity
 *
 * A simple demonstration of how the hash function for the String class works.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HashActivity extends Activity {

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checksum_activity);

		final EditText et = (EditText) findViewById(R.id.editText);

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

		Button btnCalculate = (Button) findViewById(R.id.btnCalculate);
		btnCalculate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = et.getText().toString();
				byte bytes[] = msg.getBytes();

				tv.setText("String.hashCode():   " + msg.hashCode());

				tv.append("\nhashcode(): " + hashcode(msg.toCharArray()));
			}
		});
	}

	public int hashcode(char[] value) {
		int h = 0;// hash;
		if (h == 0 && value.length > 0) {
			char[] val = value;

			for (int i = 0; i < value.length; i++) {
				h = 31 * h + val[i];
			}
			// hash = h;
		}
		return h;
	}

	public int hashcode2(char[] value) {
		int h = 0;
		for (int i = 0; i < value.length; i++) {
			h = 31 * h + value[i];
		}
		return h;
	}
}