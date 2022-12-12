package variationenzumthema_ch7;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TimeServerActivity
 *
 * This activity implements a simple date and time server using the
 * ServerSocket.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TimeServerActivity extends Activity implements Runnable {
	private final int PORT_TIME = 3737;

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("My IP: " + Util.getMyLocalIpAddress().getHostAddress() + "\n");

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(PORT_TIME);
			while (true) {
				Socket socket = server.accept();

				final String clientIP = socket.getInetAddress().getHostAddress();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv.append("- " + clientIP + "\n");
					}
				});

				OutputStream os = socket.getOutputStream();
				String daytime = new Date().toString();
				os.write(daytime.getBytes());

				os.close();
				socket.close();
			}
			// server.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
