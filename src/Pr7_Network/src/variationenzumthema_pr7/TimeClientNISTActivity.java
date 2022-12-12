package variationenzumthema_pr7;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TimeClientNISTActivity
 *
 * This activity demonstrates how to connect to the NIST time and date servers.
 * 
 * Time Protocol (RFC-868): Time represents the time in UTC seconds since
 * January 1, 1900. <br/>
 * Daytime Protocol (RFC-867): JJJJJ YR-MO-DA HH:MM:SS TT L H msADV UTC(NIST)
 * OTM<br/>
 * 
 * @see https
 *      ://www.nist.gov/pml/time-and-frequency-division/services/internet-time
 *      -service-its
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TimeClientNISTActivity extends Activity {
	private final String IP = "time.nist.gov";
	private final int PORT_TIME = 37;
	private final int PORT_DAYTIME = 13;
	private final int TIMEOUT = 1000; // network timeout in ms

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		TextView tv = (TextView) findViewById(R.id.textview);

		tv.setText("Local Time: " + getLocalTime() + "\n");
		tv.append("Local Time - 2h: " + (getLocalTime() - 3600) + "\n");
		tv.append("NIST UTCTime: " + getTimeFromNIST() + "\n");
		tv.append("NIST Daytime: " + getDayTimeFromNIST() + "\n");
	}

	/**
	 * @return time in seconds since January 1, 1900.
	 */
	private long getTimeFromNIST() {
		long time = 0;
		try {
			SocketAddress sockaddr = new InetSocketAddress(IP, PORT_TIME);
			Socket socket = new Socket();
			socket.connect(sockaddr, TIMEOUT);

			InputStream is = socket.getInputStream();

			byte[] buffer = new byte[4];
			int len = is.read(buffer, 0, 4);
			time = 4294967296l + Util.byteArrayToBigEndianInt(buffer);

			is.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * @return time in seconds since January 1, 1900.
	 */
	private String getDayTimeFromNIST() {
		String daytime = "";
		try {
			SocketAddress sockaddr = new InetSocketAddress(IP, PORT_DAYTIME);
			Socket socket = new Socket();
			socket.connect(sockaddr, TIMEOUT);

			InputStream is = socket.getInputStream();
			while (true) {
				int len = is.read();
				if (len == -1)
					break;
				daytime += (char) len;
			}

			is.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return daytime;
	}

	private long getLocalTime() {
		long localTime = (new Date().getTime() - new Date(0, 0, 1).getTime()) / 1000;
		return localTime;
	}

}
