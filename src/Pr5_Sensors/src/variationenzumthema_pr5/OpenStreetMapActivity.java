package variationenzumthema_pr5;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * OpenStreetMapActivity
 *
 * This activity displays your current position in OpenStreetMaps. You need to
 * turn GPS on for this to work.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class OpenStreetMapActivity extends Activity implements LocationListener {

	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// get best provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);

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
		double lon = location.getLongitude();
		double lat = location.getLatitude();

		String msg = "lat=" + lat + ", lon=" + lon;
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

		Uri uri = Uri.parse("http://www.openstreetmaps.org/?lat=" + lat + "&lon=" + lon + "&zoom=20");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
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
