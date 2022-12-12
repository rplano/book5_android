package variationenzumthema_ch4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import variationenzumthema.ch4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ResourceActivity
 *
 * This activity demonstrates how to access resources.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ResourceActivity extends Activity {
	private TextView tv;
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		try {
			InputStream is = getResources().openRawResource(R.raw.text);

			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line).append('\n');
			}

			tv.setText(total.toString());

			InputStream is2 = getResources().openRawResource(R.raw.mona_lisa);
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
