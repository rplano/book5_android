package variationenzumthema_ch1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BrowserActivity
 *
 * Shows how to start another application using an Intent.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BrowserActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("Click me!");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.variationenzumthema.de/");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}
}