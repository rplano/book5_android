package variationenzumthema_st3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NotificationActivity
 *
 * This activity allows you to stop the notification service.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NotificationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Intent notificationServiceIntent = new Intent(getApplicationContext(), NotificationService.class);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("End NotificationService");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(notificationServiceIntent);
				Toast.makeText(v.getContext(), "NotificationService stopped", Toast.LENGTH_LONG).show();
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}
}