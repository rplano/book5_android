package variationenzumthema_ch2;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MenuActivity
 *
 * This activity shows how to create and use menus in an activity.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		case R.id.calculator:
			startActivity(new Intent(this, CalculatorActivity.class));
			return true;
		}
		return false;
	}
}
