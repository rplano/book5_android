package variationenzumthema_pr2;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * IconMenuActivity
 * 
 * Shows how to add little icons to the title bar of an activity instead of a
 * menu.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class IconMenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.icon_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_previous_image:
			Log.i("IconMenuActivity", "previous");
			return true;

		case R.id.action_next_image:
			Log.i("IconMenuActivity", "next");
			return true;

		case R.id.action_rotate_image:
			Log.i("IconMenuActivity", "rotate");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
