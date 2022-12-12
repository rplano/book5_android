package variationenzumthema_ch7;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * InetAddressActivity
 *
 * This activity does a simple DNS and reverse DNS lookup using the InetAddress
 * class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class InetAddressActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		TextView tv = (TextView) findViewById(R.id.textview);

		// this is a dirty trick to avoid
		// android.os.NetworkOnMainThreadException
		// instead check out and references
		// @see
		// http://stackoverflow.com/questions/8612406/android-os-networkonmainthreadexception-need-to-use-async-task
		// @see http://developer.android.com/reference/android/os/AsyncTask.html
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		String networkInfo = "";
		try {
			InetAddress localAdr = InetAddress.getLocalHost();
			networkInfo += "Local IP: " + localAdr.getHostAddress();

			InetAddress remoteAdr = InetAddress.getByName("www.google.com");
			networkInfo += "\nGoogle IP: " + remoteAdr.getHostAddress();

			InetAddress[] remoteAdrs = InetAddress.getAllByName("www.google.com");
			for (int i = 0; i < remoteAdrs.length; i++) {
				networkInfo += "\nGoogle IP" + i + ": " + remoteAdrs[i].getHostAddress();
			}

		} catch (UnknownHostException e) {
			Log.e(getLocalClassName(), e.getMessage());
		}

		tv.setText(networkInfo);
	}
}