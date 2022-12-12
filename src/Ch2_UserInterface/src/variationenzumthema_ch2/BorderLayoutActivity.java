package variationenzumthema_ch2;

import android.app.Activity;
import android.os.Bundle;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BorderLayoutActivity
 *
 * This activity shows how to use the RelativeLayout to mimic the BorderLayout
 * from Swing (or rather AWT).
 *
 * @see https://stackoverflow.com/questions/6473132/borderlayout-in-android
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BorderLayoutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.border_layout_activity);
	}
}