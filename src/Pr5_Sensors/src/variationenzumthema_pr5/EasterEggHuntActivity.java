package variationenzumthema_pr5;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * EasterEggHuntActivity
 *
 * Easter Egg Hunt in the 21st Century: the activity starts in the 'hide' mode
 * where the eggs are hidden and the locations are stored in a list. In the
 * 'search' mode the distance between the current location and the location of
 * one of the hidden eggs is displayed in a color coded way. You need to turn
 * GPS on for this to work.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EasterEggHuntActivity extends Activity implements LocationListener {

	private final int DISTANCE_FACTOR = 6;

	private LocationManager locationManager;
	private Location currentLocation = null;
	private List<Location> locations = new ArrayList<Location>();
	private int currentEasterEgg = -1;
	private float[] hsv = { 240f, 1f, 1f };

	private LinearLayout ll;
	private Button btnAddLocation;
	private Button btnDone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUILayout();

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
		currentLocation = location;
		if (currentEasterEgg < 0) {
			String msg = "" + location.getLongitude() + ", " + location.getLatitude();
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		} else {
			float distanceInMeters = currentLocation.distanceTo(locations.get(currentEasterEgg));
			Toast.makeText(this, "Distance is " + distanceInMeters + " meters", Toast.LENGTH_SHORT).show();

			hsv[0] = distanceInMeters * DISTANCE_FACTOR;
			ll.setBackgroundColor(Color.HSVToColor(hsv));

			if (distanceInMeters < 5) {
				Toast.makeText(this, "Congratulations you found egg " + currentEasterEgg, Toast.LENGTH_SHORT).show();
				currentEasterEgg++;
				if (currentEasterEgg >= locations.size()) {
					currentEasterEgg = -1;
					Toast.makeText(this, "Congratulations you found all eggs!", Toast.LENGTH_SHORT).show();

					ll.setBackgroundColor(0x200000ff);
					btnAddLocation.setVisibility(View.VISIBLE);
					btnDone.setVisibility(View.VISIBLE);
				}
			}
		}
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

	private void createUILayout() {
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(0x200000ff);
		// ll.setBackgroundColor(Color.HSVToColor(hsv));
		ll.setPadding(30, 30, 30, 30);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// add exit button
		btnAddLocation = new Button(this);
		btnAddLocation.setText("Add location");
		btnAddLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locations.add(currentLocation);
				Toast.makeText(v.getContext(), "Location number " + locations.size() + " added!", Toast.LENGTH_SHORT)
						.show();
			}
		});
		ll.addView(btnAddLocation);

		// add exit button
		btnDone = new Button(this);
		btnDone.setText("Start Easter Egg Hunt");
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnAddLocation.setVisibility(View.INVISIBLE);
				btnDone.setVisibility(View.INVISIBLE);
				currentEasterEgg = 0;
			}
		});
		ll.addView(btnDone);

		setContentView(ll);
	}
}