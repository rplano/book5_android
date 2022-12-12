package variationenzumthema_st4;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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
 * DigitalSignatureActivity
 *
 * A simple demonstration of how to generate a digital signature.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DigitalSignatureActivity extends Activity {

	private final String ALGORITHM = "DSA";
	// "DSA"

	private final String PADDING = "SHA1withDSA";
	// "SHA1withDSA"

	private EditText etPublicKey;
	private EditText etPrivateKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.digital_signature_activity);

		KeyPair keyPair = generatePublicPrivateKeyPair();

		etPublicKey = (EditText) findViewById(R.id.etPublicKey);
		PublicKey publicKey = keyPair.getPublic();
		String base64 = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
		etPublicKey.setText(base64);

		etPrivateKey = (EditText) findViewById(R.id.etPrivateKey);
		PrivateKey privateKey = keyPair.getPrivate();
		base64 = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
		etPrivateKey.setText(base64);

		final EditText etMessage = (EditText) findViewById(R.id.etMessage);
		final EditText etSignature = (EditText) findViewById(R.id.etSignature);

		Button btnSign = (Button) findViewById(R.id.btnSign);
		btnSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = etMessage.getText().toString();
				byte[] signature = sign(msg);
				etSignature.setText(Util.byteArrayToHexString(signature));
			}
		});

		Button btnVerify = (Button) findViewById(R.id.btnVerify);
		btnVerify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = etMessage.getText().toString();
				String hexString = etSignature.getText().toString();
				byte[] signature = Util.hexStringToByteArray(hexString);
				boolean verifies = verify(msg, signature);
				if (verifies) {
					Toast.makeText(getBaseContext(), "Signature verifies!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), "Signature does NOT verify!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private KeyPair generatePublicPrivateKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
			kpg.initialize(1024, new SecureRandom());
			KeyPair pair = kpg.generateKeyPair();
			return pair;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), ALGORITHM + " is not available!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private byte[] sign(String msg) {
		try {
			String base64 = etPrivateKey.getText().toString();
			byte[] encodedKey = Base64.decode(base64, Base64.DEFAULT);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

			Signature dsaSign = Signature.getInstance(PADDING);
			dsaSign.initSign(privateKey);

			dsaSign.update(msg.getBytes());
			byte[] signature = dsaSign.sign();
			return signature;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private boolean verify(String msg, byte[] signature) {
		try {
			String base64 = etPublicKey.getText().toString();
			byte[] encodedKey = Base64.decode(base64, Base64.DEFAULT);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);

			Signature dsaSign = Signature.getInstance(PADDING);
			dsaSign.initVerify(publicKey);

			dsaSign.update(msg.getBytes());
			boolean verifies = dsaSign.verify(signature);
			return verifies;

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	public DigitalSignatureActivity() {
		String sMsg = "This is my message, some text...";

		try {
			// generate KeyPair:
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
			kpg.initialize(1024, new SecureRandom());
			KeyPair myPair = kpg.generateKeyPair();
			System.out.println(myPair);

			// sign
			// Signature dsaSign = Signature.getInstance( "DSA" );
			Signature dsaSign = Signature.getInstance(PADDING, "BC");
			PrivateKey myPrivKey = myPair.getPrivate();
			dsaSign.initSign(myPrivKey);

			dsaSign.update(sMsg.getBytes());
			byte[] signature = dsaSign.sign();
			System.out.println(signature);
			System.out.println(Util.byteArrayToHexString(signature));

			// verify signature
			// sMsg = sMsg + " ";
			PublicKey myPubKey = myPair.getPublic();
			// dsaSign = Signature.getInstance( "DSA" );
			dsaSign = Signature.getInstance("SHA1withDSA", "BC");
			dsaSign.initVerify(myPubKey);

			dsaSign.update(sMsg.getBytes());
			boolean bVerifies = dsaSign.verify(signature);
			System.out.println("Signature correct: " + bVerifies);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
