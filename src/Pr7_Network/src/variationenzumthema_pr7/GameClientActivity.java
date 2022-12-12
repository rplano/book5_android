package variationenzumthema_pr7;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GameClientActivity
 *
 * This activity connects to a local UDP server to find other GameClients.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GameClientActivity extends Activity implements Runnable {
	private final int GAME_SERVER_PORT = 3235;

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("Trying to connect to GameServer...\n");

		// first send UDP message to server
		sendYoToEveryone();

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
			// then wait for TCP answer from server
			ServerSocket server = null;
			server = new ServerSocket(GAME_SERVER_PORT);
			Socket socket = server.accept();

			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			final InetAddress[] addresses = (InetAddress[]) ois.readObject();
			ois.close();
			is.close();
			socket.close();
			server.close();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String msg = "\nYou team:\n";
					msg += "[";
					for (int i = 0; i < addresses.length; i++) {
						msg += addresses[i].getHostAddress() + ", ";
					}
					msg = msg.substring(0, msg.length() - 2);
					msg += "]\n";
					tv.append(msg);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendYoToEveryone() {
		try {
			InetAddress globalBroadcastAddress = Util.getLocalBroadcastAddress();
			tv.append("Broadcast to: " + globalBroadcastAddress.getHostAddress() + "\n");

			byte[] data = "Yo".getBytes();
			DatagramPacket theOutput = new DatagramPacket(data, data.length, globalBroadcastAddress, GAME_SERVER_PORT);
			DatagramSocket theSocket = new DatagramSocket();
			theSocket.send(theOutput);
			theSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
