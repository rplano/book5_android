package variationenzumthema_st4;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

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
 * AsymmetricEncryptionActivity
 *
 * A simple demonstration of asymmetric encryption.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AsymmetricEncryptionActivity extends Activity {

	private final String ALGORITHM = "RSA";
	// "ElGamal", "RSA", "RSA"

	private final String PADDING = "RSA/ECB/PKCS1Padding";
	// "ElGamal", "RSA/ECB/PKCS1Padding", "RSA"
	// ECB;CBC;CFB;OFB

	private EditText etPublicKey;
	private EditText etPrivateKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asymmetric_encryption_activity);

		KeyPair keyPair = generatePublicPrivateKeyPair();

		etPublicKey = (EditText) findViewById(R.id.etPublicKey);
		PublicKey publicKey = keyPair.getPublic();
		String base64 = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
		etPublicKey.setText(base64);

		etPrivateKey = (EditText) findViewById(R.id.etPrivateKey);
		PrivateKey privateKey = keyPair.getPrivate();
		base64 = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
		etPrivateKey.setText(base64);

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

	private KeyPair generatePublicPrivateKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
			kpg.initialize(2048, new SecureRandom());
			KeyPair pair = kpg.generateKeyPair();
			return pair;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), ALGORITHM + " is not available!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private byte[] encrypt(String msg) {
		try {
			String base64 = etPublicKey.getText().toString();
			byte[] encodedKey = Base64.decode(base64, Base64.DEFAULT);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);

			Cipher cipher = Cipher.getInstance(PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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
			String base64 = etPrivateKey.getText().toString();
			byte[] encodedKey = Base64.decode(base64, Base64.DEFAULT);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

			Cipher cipher = Cipher.getInstance(PADDING);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decrypted = cipher.doFinal(encrypted);
			String msg = new String(decrypted, "UTF-8");
			return msg;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	// public AsymmetricEncryptionActivity() {
	// String algk = "ElGamal"; // "RSA", "RSA"
	// String algc = "ElGamal"; // "RSA/ECB/PKCS1Padding", "RSA"
	// // ECB;CBC;CFB;OFB
	//
	// String sMsg = "This is my message, some text...";
	//
	// try {
	// // add provider:
	// // Security.addProvider(new
	// // org.bouncycastle.jce.provider.BouncyCastleProvider());
	//
	// // generate KeyPair:
	// KeyPairGenerator kpg = KeyPairGenerator.getInstance(algk);
	// kpg.initialize(2048, new SecureRandom());
	// KeyPair myPair = kpg.generateKeyPair();
	// System.out.println(myPair);
	//
	// // encrypt:
	// Cipher encrypt = Cipher.getInstance(algc);
	// encrypt.init(Cipher.ENCRYPT_MODE, myPair.getPublic());
	// byte[] encryptedText = encrypt.doFinal(sMsg.getBytes());
	// System.out.println(encryptedText.length);
	//
	// // decrypt:
	// Cipher decrypt = Cipher.getInstance(algc);
	// decrypt.init(Cipher.DECRYPT_MODE, myPair.getPrivate());
	// byte[] decrypted = decrypt.doFinal(encryptedText);
	// String sNew = new String(decrypted);
	// System.out.println(sNew);
	//
	// } catch (Exception e) {
	// System.out.println(e);
	// }
	// }
	//
	// public static void main(String[] args) {
	// AsymmetricEncryptionActivity ae = new AsymmetricEncryptionActivity();
	// }
}
