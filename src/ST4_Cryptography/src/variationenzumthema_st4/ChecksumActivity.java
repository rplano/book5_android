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

import java.util.zip.CRC32;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ChecksumActivity
 *
 * A simple demonstration of CRC-32 and Adler-32.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ChecksumActivity extends Activity {

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

				Checksum crc32 = new CRC32();
				crc32.update(bytes, 0, bytes.length);
				long value = crc32.getValue();
				String hex = Util.byteArrayToHexString(Util.longToBytes(value)).substring(8);
				tv.setText("CRC-32:   " + hex);

				Checksum adler32 = new Adler32();
				adler32.update(bytes, 0, bytes.length);
				value = adler32.getValue();
				hex = Util.byteArrayToHexString(Util.longToBytes(value)).substring(8);
				tv.append("\nAdler-32: " + hex);
			}
		});
	}
}
