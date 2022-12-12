package variationenzumthema_pr7;

import java.io.*;
import java.net.*;

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
 * TCPChatActivity
 *
 * This activity shows how to implement a chat activity using TCP.
 * 
 * @see http://stackoverflow.com/questions/5135438/example-android-bi-directional
 *      -network-socket-using-asynctask
 * @see http://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
 * 
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TCPChatActivity extends Activity {

	private final int PORT = 19192;
	private final int BUFFER_SIZE = 8192;

	/** define UI elements */
	private Button btnSend;
	private Button btnConnect;
	private Button btnStartServer;
	private TextView textConversation;
	private EditText editServerIP;
	private EditText editMessage;

	/** a mixture between separate thread and call-back */
	private ConnectionTask connectionTask;

	/** app can run either in server or in client mode */
	private boolean runAsServer = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tcp_chat_activity);
		setTitle("TCPChat - " + Util.getMyLocalIpAddress().getHostAddress());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// get references to UI elements:
		textConversation = (TextView) findViewById(R.id.textConversation);
		editServerIP = (EditText) findViewById(R.id.editServerIP);
		editMessage = (EditText) findViewById(R.id.editMessage);

		editServerIP.setText(Util.getMyLocalIpAddress().getHostAddress());

		// run as server:
		btnStartServer = (Button) findViewById(R.id.btnStartServer);
		btnStartServer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btnStartServer.setEnabled(false);
				btnConnect.setEnabled(false);
				editServerIP.setEnabled(false);
				runAsServer = true;
				connectionTask.execute();
				textConversation.setText("Started server...\r\n");
			}
		});

		// run as client:
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btnStartServer.setEnabled(false);
				btnConnect.setEnabled(false);
				editServerIP.setEnabled(false);
				runAsServer = false;
				connectionTask.execute();
				textConversation.setText("Started client...\r\n");
			}
		});

		// send a message:
		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				connectionTask.sendMessage();
				textConversation
						.setText("me: " + editMessage.getText().toString() + "\r\n" + textConversation.getText());
			}
		});

		// start asynchronuous task:
		connectionTask = new ConnectionTask();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		connectionTask.cancel(true);
	}

	public class ConnectionTask extends AsyncTask<Void, byte[], Boolean> {
		private Socket socket;
		private InputStream is;
		private OutputStream os;

		/**
		 * Gets called when the execute() method is called. It sets up the
		 * asynchronuous task. In our case it either starts the app as a TCP
		 * server or as a TCP client.
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			return startAndRunConnection(runAsServer);
		}

		/**
		 * Start a TCP server or client on the given port, and reads from the
		 * socket.
		 * 
		 * @return true if something went wrong.
		 */
		private boolean startAndRunConnection(boolean runAsServer) {
			boolean result = false;
			try {
				if (runAsServer) {
					ServerSocket server = null;
					server = new ServerSocket(PORT);
					socket = server.accept();

				} else {
					String sIP = editServerIP.getText().toString();
					SocketAddress sockaddr = new InetSocketAddress(sIP, PORT);
					socket = new Socket();
					socket.connect(sockaddr, 1000);
				}

				if (socket.isConnected()) {
					is = socket.getInputStream();
					os = socket.getOutputStream();
					receiveMessages();
				} else {
					Log.e(getLocalClassName(), "socket not connected, should not happen...");
				}

			} catch (Exception e) {
				Log.e(getLocalClassName(), "runAsServer(): " + e);
				result = true;

			} finally {
				try {
					is.close();
					os.close();
					socket.close();
				} catch (Exception e) {
					Log.e(getLocalClassName(), "runAsServer(): " + e);
					result = true;
				}
			}
			return result;
		}

		/**
		 * Listens to the input stream an forwards the data to the other thread.
		 * 
		 * @throws IOException
		 */
		private void receiveMessages() throws IOException {
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				int read = is.read(buffer, 0, BUFFER_SIZE);
				if (read == -1)
					break;
				// need to make a copy, since this is used in the other thread:
				byte[] temp = new byte[read];
				System.arraycopy(buffer, 0, temp, 0, read);
				publishProgress(temp);
			}
			Log.e(getLocalClassName(), "Done: should not happen... ");
		}

		/**
		 * Sends a message to the other guy.
		 */
		public void sendMessage() {
			String message = editMessage.getText().toString();
			try {
				if (socket.isConnected()) {
					os.write(message.getBytes());
				} else {
					Log.e(getLocalClassName(), "sendMessage(): Socket is closed");
				}

			} catch (Exception e) {
				Log.e(getLocalClassName(), "sendMessage(): " + e);
			}
		}

		/**
		 * This is the call-back: whenever messages come in this method gets
		 * called.
		 */
		@Override
		protected void onProgressUpdate(byte[]... values) {
			if (values.length > 0) {
				textConversation.setText("you: " + new String(values[0]) + "\r\n" + textConversation.getText());
			}
		}
	}
}
