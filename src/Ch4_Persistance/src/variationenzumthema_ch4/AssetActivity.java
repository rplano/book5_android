package variationenzumthema_ch4;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AssetActivity
 *
 * This activity demonstrates how to access assets.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AssetActivity extends Activity {
	private TextView tv;
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		try {
			InputStream is = getAssets().open("text.txt");

			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line).append('\n');
			}

			tv.setText(total.toString());

			InputStream is2 = getAssets().open("Mona_Lisa.jpg");
			Drawable d = Drawable.createFromStream(is2, null);
			img.setImageDrawable(d);
			img.getLayoutParams().height = 800;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		tv = new TextView(this);
		tv.setTextSize(12);
		ll.addView(tv);

		img = new ImageView(this);
		ll.addView(img);

		setContentView(ll);
	}
}
