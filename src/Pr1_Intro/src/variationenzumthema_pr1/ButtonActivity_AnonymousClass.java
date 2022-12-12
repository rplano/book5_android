package variationenzumthema_pr1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ButtonActivity
 *
 * Shows how to implement OnClickListener in an anonymous class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ButtonActivity_AnonymousClass extends Activity {

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
				Toast.makeText(v.getContext(), "Button was clicked!", Toast.LENGTH_LONG).show();
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}
}