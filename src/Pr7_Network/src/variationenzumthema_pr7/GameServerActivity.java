package variationenzumthema_pr7;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GameServerActivity
 *
 * This activity starts a UDP server that allows several clients to connect to
 * each other.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GameServerActivity extends Activity implements Runnable {
	private final int GAME_SERVER_PORT = 3235;
	private final int MAX_PACKET_SIZE = 512;
	private final int TIMEOUT = 1000; // network timeout in ms

	private final int NR_OF_PLAYERS_PER_TEAM = 2;
	private Set<InetAddress> playerSet = new HashSet<InetAddress>();

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("GameServer started.\n");

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
			byte[] buffer = new byte[MAX_PACKET_SIZE];
			DatagramSocket server = new DatagramSocket(GAME_SERVER_PORT);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			Log.i("GameServerActivity", "GameServer started, waiting for connections...");
			while (true) {
				try {
					server.receive(packet);
					InetAddress address = packet.getAddress();
					final String msg = address.getHostAddress();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append(msg + "\n");
						}
					});

					playerSet.add(address);
					if (playerSet.size() >= NR_OF_PLAYERS_PER_TEAM) {
						int i = 0;
						InetAddress[] addresses = new InetAddress[NR_OF_PLAYERS_PER_TEAM];
						for (InetAddress playerAddress : playerSet) {
							if (i < NR_OF_PLAYERS_PER_TEAM) {
								addresses[i] = playerAddress;
							}
							i++;
						}
						establishConnectionBetweenPlayers(addresses);
						playerSet.clear();
					}

					// reset the length for the next packet
					packet.setLength(buffer.length);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}
		Log.i("GameServerActivity", "GameServer finished.");
	}

	private void establishConnectionBetweenPlayers(final InetAddress[] addresses) {
		Log.i("GameServerActivity", "establishConnectionBetweenPlayers()");
		for (int i = 0; i < addresses.length; i++) {
			final InetAddress inetAddress = addresses[i];
			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {
					Log.i("GameServerActivity", "run(): " + inetAddress.getHostAddress());
					sendAddressesToClient(inetAddress, addresses);
				}

				private void sendAddressesToClient(final InetAddress inetAddress, final InetAddress[] addresses) {
					try {
						SocketAddress sockaddr = new InetSocketAddress(inetAddress, GAME_SERVER_PORT);
						Socket socket = new Socket();
						socket.connect(sockaddr, TIMEOUT);

						OutputStream os = socket.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(addresses);
						oos.close();
						os.close();
						socket.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			th.start();
		}
	}

}
