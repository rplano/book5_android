package variationenzumthema_ch2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AlarmClockActivity
 *
 * This activity demonstrates Internationalization (i18n).
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AlarmClockActivity extends Activity {
	private String[] langs = { "en", "fr", "es", "de" };
	private int countr = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_clock_activity);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "Button was clicked!", Toast.LENGTH_LONG).show();

				LocaleHelper.setLocale(AlarmClockActivity.this, langs[countr]);
				recreate();
				countr++;
				//counter = counter % 4;
				System.out.println(countr);
			}
		});
	}
}
