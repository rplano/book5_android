package variationenzumthema_st4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import javax.crypto.*;
import java.security.spec.*;
import java.util.Set;

import javax.crypto.spec.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import variationenzumthema.st4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PassPhraseEncryptionActivity
 *
 * A simple demonstration of passphrase based encryption.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PassPhraseEncryptionActivity extends Activity {

	// @see
	// https://developer.android.com/reference/javax/crypto/SecretKeyFactory
	private final String ALGORITHM = "PBEWithMD5AndDES";
	// "PBKDF2WithHmacSHA1"
	// "PBEWithMD5AndDES",
	// "PBEWithMD5AndTripleDES"

	private byte[] salt;
	private int nrOfIterations = 19;

	private File currentFile;

	private EditText etKey;
	private EditText etText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passphrase_encryption_activity);

		etKey = (EditText) findViewById(R.id.editKey);
		etText = (EditText) findViewById(R.id.editText);

		// listAlgos();

		// salt = new byte[64];
		// new SecureRandom().nextBytes(salt);
		salt = Util.hexStringToByteArray(
				"E34D94C5B9B09BC82775C76C52278C4E3ECC3EF1F303A377684E425146F7938F8537534137D110904A7AD228BB3D72BC2A702EBF966BD342E4BD242BF8A4899547899326CCD14A9C797157238F4A354E");

		String basDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
		currentFile = new File(basDir + "/Safe");
		if (!currentFile.exists()) {
			try {
				currentFile.createNewFile();
			} catch (IOException e) {
				Toast.makeText(this, "Could not create file: " + currentFile.getAbsolutePath(), Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_open_file:
			// etKey.setInputType(InputType.TYPE_CLASS_TEXT |
			// InputType.TYPE_TEXT_VARIATION_PASSWORD);
			loadFromFile();
			return true;

		case R.id.action_save_file:
			saveToFile();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void loadFromFile() {
		if (currentFile.exists()) {
			byte[] encrypted = new byte[(int) currentFile.length()];
			try {
				FileInputStream fis = new FileInputStream(currentFile);
				fis.read(encrypted);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String msg = decrypt(encrypted);
			etText.setText(msg);
		} else {
			Toast.makeText(this, "File " + currentFile.getName() + " does not exist.", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveToFile() {
		if (currentFile.exists()) {
			String msg = etText.getText().toString();
			byte[] encrypted = encrypt(msg);

			try {
				FileOutputStream fos = new FileOutputStream(currentFile);
				fos.write(encrypted);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void listAlgos() {
		Provider[] providers = Security.getProviders();
		for (Provider provider : providers) {
			Log.i("CRYPTO", "provider: " + provider.getName());
			Set<Provider.Service> services = provider.getServices();
			for (Provider.Service service : services) {
				Log.i("CRYPTO", "  algorithm: " + service.getAlgorithm());
			}
		}
	}

	private byte[] encrypt(String msg) {
		try {
			String passPhrase = etKey.getText().toString();
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1024, 256);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey secretKey = keyFactory.generateSecret(keySpec);

			AlgorithmParameterSpec algSpec = new PBEParameterSpec(salt, nrOfIterations);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, algSpec);
			byte[] encrypted = cipher.doFinal(msg.getBytes());

			return encrypted;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private String decrypt(byte[] encrypted) {
		try {
			String passPhrase = etKey.getText().toString();
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, 1024, 256);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			SecretKey secretKey = keyFactory.generateSecret(keySpec);

			AlgorithmParameterSpec algSpec = new PBEParameterSpec(salt, nrOfIterations);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, algSpec);
			byte[] decrypted = cipher.doFinal(encrypted);
			String msg = new String(decrypted, "UTF-8");
			return msg;

		} catch (javax.crypto.BadPaddingException e) {
			Toast.makeText(getBaseContext(), "Did you enter the correct passphrase?", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
}
