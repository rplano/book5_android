package variationenzumthema_pr7;

import java.util.List;
import java.util.TreeMap;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * IndoorLocationActivity
 * 
 * Uses local wifi connections signal strength to estimate location.
 *
 * @see http://androidexample.com/
 * Scan_wifi_connections_or_Get_number_of_available_wifi_connections
 * /index.php?view=article_discription&aid=90&aaid=114
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class IndoorLocationActivity extends Activity {

	enum STATE {
		IDLE, SCANNING_MODE, LOCATION_MODE
	}

	private TextView tv;
	private EditText edt;

	private STATE state = STATE.IDLE;

	private WifiManager wifiManager;
	private WifiLocationManager wifiLocMangr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.indoor_location_activity);

		Button btnShowAP = (Button) this.findViewById(R.id.buttonShowAPs);
		btnShowAP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tv.setText(wifiLocMangr.listAccessPoints());
			}
		});

		edt = (EditText) this.findViewById(R.id.editText);

		Button btnScanWifi = (Button) this.findViewById(R.id.buttonScanWifi);
		btnScanWifi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				state = STATE.SCANNING_MODE;
				wifiManager.startScan();
				tv.setText("Starting Scan...");
			}
		});

		Button btnWhereAmI = (Button) this.findViewById(R.id.buttonWhereAmI);
		btnWhereAmI.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				state = STATE.LOCATION_MODE;
				wifiManager.startScan();
				tv.setText("Finding location...");
			}
		});

		tv = (TextView) this.findViewById(R.id.textView);

		wifiLocMangr = new WifiLocationManager();

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled() == false) {
			Toast.makeText(getApplicationContext(), "wifi is disabled..enabling it", Toast.LENGTH_LONG).show();
			wifiManager.setWifiEnabled(true);
		}

		WifiReceiver wifiReceiver = new WifiReceiver();
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	class WifiReceiver extends BroadcastReceiver {

		public void onReceive(Context c, Intent intent) {
			if (state == STATE.SCANNING_MODE) {
				List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
				int loctn = Integer.parseInt(edt.getText().toString());
				wifiLocMangr.addScanResults(wifiScanResultList, loctn);
				tv.setText(wifiLocMangr.listAccessPoints());

			} else if (state == STATE.LOCATION_MODE) {
				List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
				TreeMap<Integer, Integer> votes = wifiLocMangr.getLocationEstimates(wifiScanResultList);

				// print votes
				String msg = "";
				for (int vote : votes.keySet()) {
					msg += "loc: " + votes.get(vote) + ", count: " + vote + "\n";
				}
				tv.setText(msg);

			}
			state = STATE.IDLE;
		}
	}
}