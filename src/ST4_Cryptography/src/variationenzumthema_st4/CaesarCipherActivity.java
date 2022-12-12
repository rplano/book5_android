package variationenzumthema_st4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import variationenzumthema.st4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * CaesarCipherActivity
 *
 * A simple demonstration of Caesar's cipher.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CaesarCipherActivity extends Activity {

	private EditText etKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.symmetric_encryption_activity);

		String secretKey = "3";

		etKey = (EditText) findViewById(R.id.etKey);
		etKey.setText(secretKey);

		final EditText etOriginal = (EditText) findViewById(R.id.etOriginal);
		final EditText etEncrypted = (EditText) findViewById(R.id.etEncrypted);

		Button btnEncrypt = (Button) findViewById(R.id.btnEncrypt);
		btnEncrypt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = etOriginal.getText().toString().toLowerCase();
				String encrypted = encrypt(msg);
				etEncrypted.setText(encrypted);
			}
		});

		Button btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
		btnDecrypt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String encrypted = etEncrypted.getText().toString().toLowerCase();
				String msg = decrypt(encrypted);
				etOriginal.setText(msg);
			}
		});
	}

	private String encrypt(String msg) {
		int key = Integer.parseInt(etKey.getText().toString());
		String enc = "";
		for (int i = 0; i < msg.length(); i++) {
			enc += encryptChar(msg.charAt(i), key);
		}
		return enc;
	}

	private String decrypt(String encrypted) {
		int key = Integer.parseInt(etKey.getText().toString());
		String dec = "";
		for (int i = 0; i < encrypted.length(); i++) {
			dec += decryptChar(encrypted.charAt(i), key);
		}
		return dec;
	}

	private char decryptChar(char c, int key) {
		int d = c - 'a';
		int e = d - key;
		int f = e % 26;
		char g = (char) (f + 'a');
		return g;
	}

	private char encryptChar(char c, int key) {
		int d = c - 'a';
		int e = d + key;
		int f = e % 26;
		char g = (char) (f + 'a');
		return g;
	}
}
