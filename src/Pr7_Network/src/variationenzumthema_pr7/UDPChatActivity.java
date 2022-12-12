package variationenzumthema_pr7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * UDPChatActivity
 *
 * This activity shows how to implement a chat activity using UDP.
 * 
 * @see http://stackoverflow.com/questions/5135438/example-android-bi-directional
 *      -network-socket-using-asynctask
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class UDPChatActivity extends Activity {

	private final int PORT = 19192;
	private final int MAX_PACKET_SIZE = 8192;

	private TextView tv;
	private EditText et;

	private ConnectionTask connectionTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.udp_chat_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Button btnSendToAll = (Button) findViewById(R.id.btnSendToAll);
		tv = (TextView) findViewById(R.id.textConversation);
		et = (EditText) findViewById(R.id.editMessage);

		connectionTask = new ConnectionTask();
		connectionTask.execute();

		btnSendToAll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendMessage();
			}
		});
	}

	/**
	 * Sends a UDP datagram packet to a given IP address
	 */
	private void sendMessage() {
		try {
			String sMessage = et.getText().toString();
			DatagramSocket ds = new DatagramSocket();
			InetAddress serverAddr = Util.getLocalBroadcastAddress();
			DatagramPacket dp = new DatagramPacket(sMessage.getBytes(), sMessage.length(), serverAddr, PORT);
			ds.setBroadcast(true);
			ds.send(dp);
			Log.i(getLocalClassName(), "sendMessage(): " + sMessage);

		} catch (Exception e) {
			Log.e(getLocalClassName(), "sendMessage(): " + e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		connectionTask.cancel(true);
	}

	public class ConnectionTask extends AsyncTask<Void, byte[], Boolean> {

		/**
		 * Gets called when the execute() method is called. It sets up the
		 * asynchronous task. In our case it either starts the app as a TCP
		 * server or as a TCP client.
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean result = false;

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			// String lText;
			byte[] buffer = new byte[MAX_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			DatagramSocket socket = null;
			try {
				socket = new DatagramSocket(PORT);
				// ds.setSoTimeout(100000);
				while (true) {
					try {
						socket.receive(packet);

						InetAddress senderIP = packet.getAddress();
						String senderName = "" + senderIP.getAddress()[3];

						byte[] temp = new byte[packet.getLength()];
						System.arraycopy(buffer, 0, temp, 0, temp.length);
						publishProgress(senderName.getBytes(), temp);

					} catch (IOException e) {
						Log.e(getLocalClassName(), "doInBackground: " + e);
					}

				}

			} catch (Exception e) {
				Log.e(getLocalClassName(), "doInBackground: " + e);
				result = true;

			} finally {
				socket.close();
			}

			return result;
		}

		/**
		 * This is the call-back: whenever messages come in this method gets
		 * called.
		 */
		@Override
		protected void onProgressUpdate(byte[]... values) {
			if (values.length > 0) {
				String name = new String(values[0]);
				String msg = new String(values[1]);
				tv.setText(name + ": " + msg + "\r\n" + tv.getText());
			}
		}
	}
}
