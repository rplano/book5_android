package variationenzumthema_pr7;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * JSONBooksActivity
 *
 * This activity downloads JSON from Google books and parses it with the JSONObject class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class JSONBooksActivity extends Activity {
	private final int TIMEOUT = 1000; // network timeout in ms

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.json_books_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		final TextView tv = (TextView) findViewById(R.id.textview);

		final EditText et = (EditText) findViewById(R.id.edittext);
		et.setText("9780521427067");

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String isbn = et.getText().toString();
				String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
				String json = Util.getWebpage(url);
				Book bk = new Book(json);
				tv.setText("" + bk + "\n");
			}
		});
	}
}

class Book {
	private String title;
	private String author;

	public Book(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray items = jsonObject.getJSONArray("items");
			JSONObject book1 = (JSONObject) items.get(0);

			JSONObject volumeInfo = (JSONObject) book1.get("volumeInfo");

			title = volumeInfo.getString("title");

			JSONArray authors = volumeInfo.getJSONArray("authors");
			author = (String) authors.get(0);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return "Book [author=" + author + ", title=" + title + "]";
	}

}
