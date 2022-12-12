package variationenzumthema_pr7;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NFCActivity
 *
 * This activity lists NCF and RFID devices nearby. It gets launched through an
 * intent-filter in the Manifest.
 * 
 * @see http://android-er.blogspot.com/2015/10/android-nfc-example-to-read-tag-info-of.html
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NFCActivity extends Activity {
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("Please place an RFID tag near your phone...");

		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "Your device does not support NFC!", Toast.LENGTH_LONG).show();
		} else if (!nfcAdapter.isEnabled()) {
			Toast.makeText(this, "You need to enable NFC for this app to work!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Intent intent = getIntent();
		String action = intent.getAction();

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			if (tag != null) {
				String tmp = "Id: " + Util.byteArrayToHexString(tag.getId()) + "\n\n";

				tmp += "Supported NFC Technologies:\n";
				String[] technologies = tag.getTechList();
				for (int i = 0; i < technologies.length; i++) {
					tmp += "- " + technologies[i] + "\n";
				}

				tv.setText(tmp);
			}
		}
	}

}
