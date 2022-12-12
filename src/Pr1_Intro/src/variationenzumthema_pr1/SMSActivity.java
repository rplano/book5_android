package variationenzumthema_pr1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SMSActivity
 *
 * Shows how to send an SMS using an Intent.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SMSActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("Send SMS");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                smsIntent.putExtra("sms_body", "my first sms from an android app!");
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + "0123 456 7890"));
                startActivity(smsIntent);
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}
}