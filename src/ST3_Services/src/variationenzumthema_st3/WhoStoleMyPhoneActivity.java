package variationenzumthema_st3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WhoStoleMyPhoneActivity
 *
 * This activity only starts the service.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WhoStoleMyPhoneActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent whoStoleMyPhoneServiceIntent = new Intent(getApplicationContext(), WhoStoleMyPhoneService.class);
		startService(whoStoleMyPhoneServiceIntent);
	}
}