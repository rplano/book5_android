package variationenzumthema_st4;

import java.security.MessageDigest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.st4.R;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * HashCashActivity
 *
 * A simple demonstration of the HashCash algorithm. This is the basis of the
 * proof-of-work for bitcoins.
 * 
 * @see Hashcash, https://en.wikipedia.org/wiki/Hashcash
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HashCashActivity extends Activity {

	private EditText et;
	private TextView tv;
	private int nrOfZeros = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hash_cash_activity);

		et = (EditText) findViewById(R.id.edittext);

		tv = (TextView) findViewById(R.id.textview);

		Integer[] zeros = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, zeros);
		Spinner sp = (Spinner) findViewById(R.id.spinner);
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				nrOfZeros = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv.setText("");
				String msg = et.getText().toString();
				long start = System.currentTimeMillis();
				String hexHash = findHashWithNZeros(msg, nrOfZeros);
				long time = System.currentTimeMillis() - start;
				Toast.makeText(getBaseContext(), "" + time + " ms", Toast.LENGTH_LONG).show();
				tv.setText(hexHash);
			}

			private String findHashWithNZeros(String msg, int nrOfZeros) {
				String hex = "?";
				try {
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					long counter = 0;

					while (true) {
						// find fixerUpper
						byte[] fixerUpper = Util.longToBytes(counter);

						// add fixerUpper to msg
						String x = msg;
						for (int k = 0; k < fixerUpper.length; k++) {
							x += (char) fixerUpper[k];
						}

						// calc SHA-256
						md.reset();
						byte[] cryptoHash = md.digest(x.getBytes("UTF-8"));

						// did we find it?
						if (areNBytesZero(cryptoHash, nrOfZeros)) {
							hex = Util.byteArrayToHexString(cryptoHash);
							return hex;
						}

						counter++;
					}

				} catch (Exception e) {
					System.out.println(e);
				}
				return hex;
			}

			// not very elegant or fast
			private boolean areNBytesZero(byte[] cryptoHash, int nrOfZeros) {
				String hex = Util.byteArrayToHexString(cryptoHash);
				for (int i = 0; i < nrOfZeros; i++) {
					if (hex.charAt(i) != '0')
						return false;
				}
				return true;
			}
		});

	}

}