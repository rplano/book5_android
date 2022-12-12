package variationenzumthema_pr7;

import java.net.InetAddress;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NetworkScannerActivity
 *
 * This activity tries to find computer in the local network using ICMP.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NetworkScannerActivity extends Activity implements Runnable {
	private final int TIMEOUT = 500; // network timeout in ms
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
			InetAddress myIP = Util.getMyLocalIpAddress();
			tv.setText("My IP: " + myIP.getHostAddress() + "\n");

			byte[] localAddresses = myIP.getAddress();
			for (int i = 0; i < 256; i++) {
				localAddresses[3] = (byte) i;
				final InetAddress address = InetAddress.getByAddress(localAddresses);
				if (address.isReachable(TIMEOUT)) {
					// Log.i("NetworkScannerActivity",
					// address.getHostAddress());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("" + address.getHostAddress() + "\n");
						}
					});
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
