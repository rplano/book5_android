package variationenzumthema_pr1;

import java.util.Calendar;

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
 * CalendarActivity
 *
 * Shows how to make an entry in the users calendar.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CalendarActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("New calendar entry");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar beginTime = Calendar.getInstance();
				beginTime.set(2017, 8, 1, 16, 00);	//year, month, day, hour, minute
				long startMillis = beginTime.getTimeInMillis();
				Calendar endTime = Calendar.getInstance();
				endTime.set(2017, 8, 1, 17, 00);		//year, month, day, hour, minute
				long endMillis = endTime.getTimeInMillis();

				Intent intent = new Intent(Intent.ACTION_EDIT);  
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("title", "Some title");
				intent.putExtra("description", "Some description");
				intent.putExtra("beginTime", startMillis);
				intent.putExtra("endTime", endMillis);
				startActivity(intent);
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}
}