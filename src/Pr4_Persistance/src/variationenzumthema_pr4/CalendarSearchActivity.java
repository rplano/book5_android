package variationenzumthema_pr4;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * CalendarSearchActivity
 *
 * This activity searches in your calendar for title or location. It uses the
 * SQL "LIKE" operator and is a little dangerous.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CalendarSearchActivity extends Activity {

	private EditText etTitle;
	private EditText etLocation;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_search_activity);

		etTitle = (EditText) findViewById(R.id.et_title);
		etLocation = (EditText) findViewById(R.id.et_location);

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = etTitle.getText().toString();
				if (title != null && title.length() > 0) {
					tv.setText(searchTitleFromEventTable(title));
				} else {
					String location = etLocation.getText().toString();
					tv.setText(searchLocationFromEventTable(location));
				}
			}
		});

	}

	private String searchTitleFromEventTable(String title) {
		String titles = "";

		String[] projection = { "_id", CalendarContract.Events.TITLE, CalendarContract.Events.EVENT_LOCATION,
				CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, };

		Uri uri = CalendarContract.Events.CONTENT_URI;
		String selection = CalendarContract.Events.TITLE + " LIKE ? ";
		String[] selectionArgs = new String[] { title };

		Cursor cur = getContentResolver().query(uri, projection, selection, selectionArgs, null);

		while (cur.moveToNext()) {
			String titl = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
			titl += ":" + cur.getString(cur.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
			titles += titl + "\n";
		}
		cur.close();

		return titles;
	}

	private String searchLocationFromEventTable(String location) {
		String locations = "";

		String[] projection = { "_id", CalendarContract.Events.TITLE, CalendarContract.Events.EVENT_LOCATION,
				CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, };

		Uri uri = CalendarContract.Events.CONTENT_URI;
		String selection = CalendarContract.Events.EVENT_LOCATION + " LIKE ? ";
		String[] selectionArgs = new String[] { location };

		Cursor cur = getContentResolver().query(uri, projection, selection, selectionArgs, null);

		while (cur.moveToNext()) {
			String titl = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
			titl += ":" + cur.getString(cur.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
			locations += titl + "\n";
		}
		cur.close();

		return locations;
	}
}
