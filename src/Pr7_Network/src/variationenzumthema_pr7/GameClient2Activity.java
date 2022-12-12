package variationenzumthema_pr7;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GameClient2Activity
 *
 * This activity connects to an internet service to find other GameClients in
 * the same local network. It uses the ConnectionBroker class to do this.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GameClient2Activity extends Activity implements Runnable {

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("Trying to connect to GameServer...\n");

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		String myLocalIP = Util.getMyLocalIpAddress().getHostAddress();

		// establish connection to other player
		ConnectionBroker broker = new ConnectionBroker();
		broker.start();
		while (!broker.isSocketAvailable()) {
			pause(200);
		}

		// get InputStream and OutputStream
		try {
			final Socket socket = broker.getSocket();
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			if (broker.isServer()) {
				ObjectInputStream ois = new ObjectInputStream(is);
				String msgReceived = (String) ois.readObject();
				showInUIThread(msgReceived);

				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(myLocalIP);

			} else {
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(myLocalIP);

				ObjectInputStream ois = new ObjectInputStream(is);
				String msgReceived = (String) ois.readObject();
				showInUIThread(msgReceived);
			}

			os.close();
			is.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showInUIThread(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tv.append("msgReceived: " + msg);
			}
		});
	}

	public void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
