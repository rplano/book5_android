package variationenzumthema_ch5;

import java.util.List;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.ch5.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * LocationActivity
 *
 * This activity lists the different location provider on the device, and shows
 * location if available. <br/>
 * On the emulator make sure that GPS is turned on.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class LocationActivity extends Activity implements LocationListener {

	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);
		TextView tv = (TextView) findViewById(R.id.textview);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// list all providers
		String msg = "Available Providers are:\n";
		List<String> providers = locationManager.getAllProviders();
		for (String providerName : providers) {
			LocationProvider provider = locationManager.getProvider(providerName);
			msg += "- " + provider.getName() + ", ";
			msg += provider.getAccuracy() + ", ";
			msg += provider.getPowerRequirement() + "\n";
		}
		tv.setText(msg);

		// get best provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		tv.append("\nBest Provider is: " + bestProvider);

		// request location updates: min time, min distance
		locationManager.requestLocationUpdates(bestProvider, 1000, 1, this);
	}

	@Override
	public void onPause() {
		locationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	public void onLocationChanged(Location location) {
		String msg = "" + location.getLongitude() + ", " + location.getLatitude();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
