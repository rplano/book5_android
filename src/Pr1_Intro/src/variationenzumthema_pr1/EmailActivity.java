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
 * EmailActivity
 *
 * Shows how to send an Email using an Intent.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EmailActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("Send Email");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "ralph@lano.de", null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Book");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "I like your book!");
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}
}