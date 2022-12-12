package variationenzumthema_ch7;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * URLActivity
 *
 * This activity downloads a web page using the URL class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class URLActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText(getWebpage("http://www.lano.de"));
	}

	public static String getWebpage(String address) {
		try {
			URL url = new URL(address);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream is = con.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String content = "";
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				content += line;
			}
			br.close();
			con.disconnect();

			return content;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}