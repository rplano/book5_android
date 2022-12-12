package variationenzumthema_st4;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import variationenzumthema.st4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SymmetricEncryptionActivity
 *
 * A simple demonstration of symmetric encryption. Try to avoid using ECB,
 * instead use CBC operating mode.
 *
 * @see Encryption operating modes: ECB vs CBC,
 *      https://www.adayinthelifeof.nl/2010/12/08/encryption-operating-modes-ecb-vs-cbc/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SymmetricEncryptionActivity extends Activity {

	private final String ALGORITHM = "AES";
	// "AES", "IDEA", "Skipjack", "DES", "TripleDES", "Blowfish"

	private final String PADDING = "AES/ECB/PKCS7Padding";
	// "AES/ECB/PKCS7Padding"
	// "IDEA/ECB/PKCS5Padding",
	// "Skipjack/ECB/PKCS5Padding"
	// "DES/ECB/PKCS5Padding",
	// "TripleDES/ECB/PKCS5Padding",
	// "Blowfish/ECB/PKCS5Padding"
	// ECB;CBC;CFB;OFB

	private EditText etKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.symmetric_encryption_activity);

		SecretKey secretKey = generateSymmetricKey();

		etKey = (EditText) findViewById(R.id.etKey);
		String base64 = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
		etKey.setText(base64);

		final EditText etOriginal = (EditText) findViewById(R.id.etOriginal);
		final EditText etEncrypted = (EditText) findViewById(R.id.etEncrypted);

		Button btnEncrypt = (Button) findViewById(R.id.btnEncrypt);
		btnEncrypt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = etOriginal.getText().toString();
				byte[] encrypted = encrypt(msg);
				etEncrypted.setText(Util.byteArrayToHexString(encrypted));
			}
		});

		Button btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
		btnDecrypt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String hexString = etEncrypted.getText().toString();
				byte[] encrypted = Util.hexStringToByteArray(hexString);
				String msg = decrypt(encrypted);
				etOriginal.setText(msg);
			}
		});
	}

	private SecretKey generateSymmetricKey() {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
			kgen.init(256); // use a 256 bit length
			// AES: key length of 128, 192, 256
			// Blowfish key length of 32 to 448 bits
			SecretKey skey = kgen.generateKey();
			return skey;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), ALGORITHM + " is not available!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private byte[] encrypt(String msg) {
		try {
			String base64 = etKey.getText().toString();
			byte[] encodedKey = Base64.decode(base64, Base64.DEFAULT);
			SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGORITHM);

			Cipher cipher = Cipher.getInstance(PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = cipher.doFinal(msg.getBytes("UTF-8"));
			return encrypted;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private String decrypt(byte[] encrypted) {
		try {
			String base64 = etKey.getText().toString();
			byte[] encodedKey = Base64.decode(base64, Base64.DEFAULT);
			SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGORITHM);

			Cipher cipher = Cipher.getInstance(PADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decrypted = cipher.doFinal(encrypted);
			String msg = new String(decrypted, "UTF-8");
			return msg;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
}
