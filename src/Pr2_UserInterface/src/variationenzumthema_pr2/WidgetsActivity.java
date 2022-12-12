package variationenzumthema_pr2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * WidgetsActivity
 * 
 * An activity that demonstrates the use of the more common widgets.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WidgetsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = createUILayout();

		TextView tv = new TextView(this);
		tv.setText("TextView");
		tv.setGravity(Gravity.CENTER);
		ll.addView(tv);

		EditText et = new EditText(this);
		et.setHint("Enter name...");
		ll.addView(et);

		String[] commands = new String[] { "Coffee", "Espresso", "Capuccino", "Latte", "Cacachino" };
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, commands);
		AutoCompleteTextView sp2 = new AutoCompleteTextView(this);
		sp2.setAdapter(adapter2);
		sp2.setHint("Autocomplete... (type coffee)");
		ll.addView(sp2);

		Button btn = new Button(this);
		btn.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
		btn.setText("Button");
		ll.addView(btn);

		CheckBox cb = new CheckBox(this);
		cb.setText("CheckBox");
		ll.addView(cb);

		RadioButton rb1 = new RadioButton(this);
		rb1.setText("Yes");
		RadioButton rb2 = new RadioButton(this);
		rb2.setText("No");
		RadioGroup rg = new RadioGroup(this);
		rg.addView(rb1);
		rg.addView(rb2);
		ll.addView(rg);

		SeekBar sb = new SeekBar(this);
		ll.addView(sb);

		ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.tictactoe_x);
		ll.addView(img);

		ImageButton imgBtn = new ImageButton(this);
		imgBtn.setImageResource(R.drawable.tictactoe_o);
		ll.addView(imgBtn);

		List<String> items = new ArrayList<String>();
		items.add("Coffee");
		items.add("Espresso");
		items.add("Capuccino");
		items.add("Latte");
		items.add("Cacachino");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		Spinner sp = new Spinner(this);
		sp.setAdapter(adapter);
		ll.addView(sp);

		NumberPicker np = new NumberPicker(this);
		np.setMinValue(1);
		np.setMaxValue(10);
		np.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ll.addView(np);

		TimePicker tp = new TimePicker(this);
		ll.addView(tp);

		DatePicker dp = new DatePicker(this);
		dp.setCalendarViewShown(false);
		ll.addView(dp);

		DatePicker dp2 = new DatePicker(this);
		dp2.setSpinnersShown(false);
		ll.addView(dp2);
	}

	private LinearLayout createUILayout() {
		LinearLayout ll1 = new LinearLayout(this);
		ll1.setOrientation(LinearLayout.VERTICAL);
		ll1.setBackgroundColor(0x200000ff);
		ll1.setPadding(30, 30, 30, 30);
		ll1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(ll1);

		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll1.addView(sv);

		LinearLayout ll2 = new LinearLayout(this);
		ll2.setOrientation(LinearLayout.VERTICAL);
		ll2.setGravity(Gravity.CENTER);
		ll2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		sv.addView(ll2);

		return ll2;
	}
}