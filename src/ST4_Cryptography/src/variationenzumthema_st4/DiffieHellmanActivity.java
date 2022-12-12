package variationenzumthema_st4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.st4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * DiffieHellmanActivity
 *
 * A simple demonstration of the Diffie-Hellman algorithm.
 *
 * @see Diffie–Hellman key exchange,
 *      https://en.wikipedia.org/wiki/Diffie–Hellman_key_exchange
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DiffieHellmanActivity extends Activity {

	// private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diffie_hellman_activity);

		final EditText et_p = (EditText) findViewById(R.id.et_p);
		final EditText et_g = (EditText) findViewById(R.id.et_g);
		final EditText et_a = (EditText) findViewById(R.id.et_a);

		final TextView tvA = (TextView) findViewById(R.id.textviewA);

		Button btnCalculateA = (Button) findViewById(R.id.btnCalculateA);
		btnCalculateA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int p = Integer.parseInt(et_p.getText().toString());
				int g = Integer.parseInt(et_g.getText().toString());
				int a = Integer.parseInt(et_a.getText().toString());
				long A = power(g, a) % p;
				tvA.setText("A= " + A);
			}
		});

		final EditText et_B = (EditText) findViewById(R.id.et_B);

		final TextView tvS = (TextView) findViewById(R.id.textviewS);

		Button btnCalculateS = (Button) findViewById(R.id.btnCalculateS);
		btnCalculateS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int p = Integer.parseInt(et_p.getText().toString());
				int a = Integer.parseInt(et_a.getText().toString());
				int B = Integer.parseInt(et_B.getText().toString());
				long s = power(B, a) % p;
				tvS.setText("s= " + s);
			}
		});
	}

	static long power(long x, long n) {
		long power = 1;
		for (int i = 0; i < n; i++) {
			power *= x;
		}
		return power;
	}
}
