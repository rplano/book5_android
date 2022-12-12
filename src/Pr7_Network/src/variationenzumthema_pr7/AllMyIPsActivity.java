package variationenzumthema_pr7;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AllMyIPsActivity
 *
 * This activity lists all network interface cards and lists their respective IP
 * addresses. In additional it also tries to determine the devices external IP
 * address.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AllMyIPsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		TextView tv = (TextView) findViewById(R.id.textview);
		tv.append("External IP:\n" + getMyExternalIP() + "\n\n");
		tv.append("All IPs:\n" + getAllLocalIpAddresses());
	}

	/**
	 * try this with <br/>
	 * - only wifi turned on: should return eth0 <br/>
	 * - only 3G turned on: should return pdp0 <br/>
	 * - 3G and wifi and tethering/hotspot: should return wl0.1 and pdp0 <br/>
	 */
	private String getAllLocalIpAddresses() {
		String sIPs = "";
		try {
			for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics
					.hasMoreElements();) {
				NetworkInterface nic = nics.nextElement();
				for (Enumeration<InetAddress> ips = nic.getInetAddresses(); ips.hasMoreElements();) {
					InetAddress ip = ips.nextElement();
					sIPs += nic.getName();
					if (InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
						sIPs += " (IPv4):";
					} else {
						sIPs += " (IPv6):";
					}
					sIPs += "  " + ip.getHostAddress() + "\n";
				}
			}
		} catch (SocketException e) {
			Log.e(getLocalClassName(), e.getMessage());
		}
		return sIPs;
	}

	private String getMyExternalIP() {
		String webpage = Util.getWebpage("http://wikimusicapp.appspot.com/myip");
		if (webpage != null) {
			String[] words = webpage.split(" ");
			return words[3];
		}
		return "No external IP available";
	}
}