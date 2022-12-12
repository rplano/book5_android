package variationenzumthema_pr7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.StringTokenizer;

import android.os.StrictMode;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ConnectionBroker
 *
 * This class connects to an internet service to find other GameClients in the
 * same local network. It is used by GameClient2Activity and TicTacToeActivity.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ConnectionBroker extends Thread {
	private final int GAME_SERVER2_PORT = 3236;
	private final int TIMEOUT = 1000; // network timeout in ms

	private String myLocalIP;
	private Socket socket;
	private boolean isSocketAvailable = false;
	private boolean isServer = false;

	public void run() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			myLocalIP = Util.getMyLocalIpAddress().getHostAddress();
			URL url = new URL("http://wikimusicapp.appspot.com/gameserver?internalIP=" + myLocalIP);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String data = br.readLine();
			br.close();

			if (data != null) {
				if (data.trim().equals("{}")) {
					startServerAndWaitToBeContacted();
				} else {
					connectToWaitingPlayer(data);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connectToWaitingPlayer(String data) {
		String ipServer = getIPAddressToConnectTo(data);
		try {
			SocketAddress sockaddr = new InetSocketAddress(ipServer, GAME_SERVER2_PORT);
			socket = new Socket();
			socket.connect(sockaddr, TIMEOUT);
			isSocketAvailable = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// {[127.0.0.1,127.0.0.2]}
	private String getIPAddressToConnectTo(String data) {
		StringTokenizer st = new StringTokenizer(data, "{[, ]}");
		while (st.hasMoreTokens()) {
			String ip = st.nextToken();
			if (!ip.equals(myLocalIP)) {
				return ip;
			}
		}
		return null;
	}

	private void startServerAndWaitToBeContacted() {
		try {
			isServer = true;
			ServerSocket server = null;
			server = new ServerSocket(GAME_SERVER2_PORT);
			socket = server.accept();
			isSocketAvailable = true;

			server.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public boolean isSocketAvailable() {
		return isSocketAvailable;
	}

	public boolean isServer() {
		return isServer;
	}
}
