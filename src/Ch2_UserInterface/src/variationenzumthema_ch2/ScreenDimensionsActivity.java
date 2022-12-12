package variationenzumthema_ch2;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ScreenDimensionsActivity
 *
 * This activity shows how to find the available screen size real estate.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ScreenDimensionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);
		getScreenSizes();
	}

	private void getScreenSizes() {
		String msg = "";
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();

		display.getRealSize(size);
		msg += "Real size: w=" + size.x + ", h=" + size.y + "\n";

		display.getSize(size);
		msg += "Fake size: w=" + size.x + ", h=" + size.y + "\n";

		// status bar height
		int statusBarHeight = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = getResources().getDimensionPixelSize(resourceId);
		}
		msg += "Status Bar: h=" + statusBarHeight + "\n";

		// action bar height
		int actionBarHeight = 0;
		final TypedArray styledAttributes = this.getTheme()
				.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
		actionBarHeight = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();
		msg += "Action Bar: h=" + actionBarHeight + "\n";

		// navigation bar height
		int navigationBarHeight = 0;
		resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
		}
		msg += "Navigation Bar: h=" + navigationBarHeight;

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.setText(msg);
	}

}
