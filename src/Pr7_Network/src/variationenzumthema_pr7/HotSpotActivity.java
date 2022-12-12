package variationenzumthema_pr7;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.pr7.R;

import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * HotSpotActivity
 *
 * This activity starts a Wifi hotspot using the HotSpotAPManager class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HotSpotActivity extends Activity {

	private TextView textView;

	private HotSpotAPManager wifiApManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotspot_activity);

		textView = (TextView) findViewById(R.id.textView1);
		textView.setText("not started yet...");

		Button btnStart = (Button) findViewById(R.id.btn_StartHotSpot);
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				textView.setText("Starting hot spot...");
				wifiApManager.setWifiApEnabled(null, true);
				try {
					Thread.yield();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Button btnCheck = (Button) findViewById(R.id.btn_CheckForClients);
		btnCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = "";
				if (wifiApManager != null) {
					List<String> clients = wifiApManager.getClientList();
					msg += "Nr of clients: " + clients.size() + "\n";
					for (String client : clients) {
						msg += "Client name: " + client + "\n";
					}

				} else {
					msg += "onClick(): wifiApManager == null";

				}
				Log.i(".HotSpot", msg);
				textView.setText(msg);
			}
		});

		Button btnStop = (Button) findViewById(R.id.btn_StopHotSpot);
		btnStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				textView.setText("Stopping hot spot...");
				wifiApManager.setWifiApEnabled(null, false);
				try {
					Thread.yield();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		wifiApManager = new HotSpotAPManager(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		wifiApManager = null;
	}
}
