package variationenzumthema_pr1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BarcodeActivity
 * 
 * Demonstrates how to use an Intent to start the barcode scanner.
 *
 * More options and details can be found in Android Cookbook, Ian F. Darwin on
 * p.204.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BarcodeActivity extends Activity {

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("Start scan");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.setPackage("com.google.zxing.client.android");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
			}
		});
		ll.addView(btn);

		tv = new TextView(this);
		ll.addView(tv);

		setContentView(ll);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				tv.setText(contents);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Scan was canceled", Toast.LENGTH_SHORT).show();
			}
		}
	}
}