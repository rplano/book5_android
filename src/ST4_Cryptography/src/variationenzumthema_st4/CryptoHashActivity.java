package variationenzumthema_st4;

import java.security.*;

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
 * CryptoHashActivity
 *
 * A simple demonstration of the crypto hash functions MD5, SHA-1 and SHA-2.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CryptoHashActivity extends Activity {

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
				try {
					String msg = et.getText().toString();
					byte bytes[] = msg.getBytes();

					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(msg.getBytes("UTF-8"));
					byte[] cryptoHash = md.digest();
					String hex = Util.byteArrayToHexString(cryptoHash);
					tv.setText("MD5:\n" + hex);

					md = MessageDigest.getInstance("SHA-1");
					md.update(msg.getBytes("UTF-8"));
					cryptoHash = md.digest();
					hex = Util.byteArrayToHexString(cryptoHash);
					tv.append("\nSHA-1:\n" + hex);

					md = MessageDigest.getInstance("SHA-256");
					md.update(msg.getBytes("UTF-8"));
					cryptoHash = md.digest();
					hex = Util.byteArrayToHexString(cryptoHash);
					tv.append("\nSHA-256:\n" + hex);

				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
	}
}
