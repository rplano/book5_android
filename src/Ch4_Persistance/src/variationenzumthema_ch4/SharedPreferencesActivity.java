package variationenzumthema_ch4;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SharedPreferencesActivity
 *
 * This activity demonstrates the use of SharedPreferences to store key/value
 * pairs.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SharedPreferencesActivity extends Activity {
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String txt = prefs.getString("KEY", "default");
		et.setText(txt);
	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("KEY", et.getText().toString());
		editor.commit();
	}

	private void createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		et = new EditText(this);
		et.setTextSize(24);
		ll.addView(et);

		setContentView(ll);
	}
}