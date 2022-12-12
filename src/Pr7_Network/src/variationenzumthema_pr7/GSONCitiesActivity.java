package variationenzumthema_pr7;

import com.google.gson.Gson;

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
 * GSONCitiesActivity
 *
 * This activity downloads JSON and creates an object out of it using the Gson
 * class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GSONCitiesActivity extends Activity {
	private final int TIMEOUT = 1000; // network timeout in ms

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gson_cities_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);

		final EditText et = (EditText) findViewById(R.id.edittext);
		et.setText("Rome");

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String cityName = et.getText().toString().toLowerCase();
				String url = "http://wikimusicapp.appspot.com/cities?city=" + cityName;
				String json = Util.getWebpage(url);

				Gson gson = new Gson();
				City city = gson.fromJson(json, City.class);
				tv.setText("" + city + "\n");
			}
		});
	}
}

class City {
	private String country;
	private String name;
	private String latitute;
	private String longitute;

	public City() {
	}

	public String toString() {
		return "City [name=" + name + ", country=" + country + ", latitute=" + latitute + ", longitute=" + longitute
				+ "]";
	}
}
