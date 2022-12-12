package variationenzumthema_pr7;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WifiScannerActivity
 *
 * This activity scans the available Wifi access point, lists their BSSID and
 * their respective signal strengths.
 * 
 * @see Scan_wifi_connections_or_Get_number_of_available_wifi_connections
 *      /index.php?view=article_discription&aid=90&aaid=114
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WifiScannerActivity extends Activity {

	private TextView tv;
	private WifiManager wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_scanner_activity);

		tv = (TextView) this.findViewById(R.id.textViewWifi);

		Button aboutButton = (Button) this.findViewById(R.id.buttonScanWifi);
		aboutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				wifiManager.startScan();
				tv.setText("Starting Scan...");
			}
		});

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled() == false) {
			Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
			wifiManager.setWifiEnabled(true);
		}

		WifiReceiver wifiReceiver = new WifiReceiver();
		registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();
		tv.setText("Starting Scan...");
	}

	class WifiReceiver extends BroadcastReceiver {

		public void onReceive(Context c, Intent intent) {
			StringBuilder sb = new StringBuilder();
			List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
			sb.append("Number Of Wifi connections :" + wifiScanResultList.size() + "\n\n");
			for (ScanResult result : wifiScanResultList) {
				String bssid = result.BSSID;
				int signalLevel = result.level;
				sb.append(bssid + ": " + signalLevel + "dB\n");
			}
			tv.setText(sb);
		}
	}
}
