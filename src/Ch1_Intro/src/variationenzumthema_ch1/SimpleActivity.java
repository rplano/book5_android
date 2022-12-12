package variationenzumthema_ch1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SimpleActivity
 *
 * A simple hello world activity.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SimpleActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		TextView tv = new TextView(this);
		tv.setTextSize(24);
		tv.setText("Hello World!");

		ll.addView(tv);
		setContentView(ll);
	}
}