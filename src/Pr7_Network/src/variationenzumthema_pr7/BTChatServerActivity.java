package variationenzumthema_pr7;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BTChatServerActivity
 *
 * This activity demonstrates how to run a bluetooth server.
 *
 * @see https://developer.android.com/guide/topics/connectivity/bluetooth#Profiles
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BTChatServerActivity extends Activity implements Runnable {
	public static final String BASE_BLUETOOTH_UUID = "-0000-1000-8000-00805F9B34FB";
	public static String MY_UUID = "00420042" + BASE_BLUETOOTH_UUID;

	private TextView tv;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothServerSocket mBluetoothServerSocket;
	private BluetoothSocket socket;
	private boolean isRunning = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("");

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		tv.append("mBluetoothAdapter: " + mBluetoothAdapter.getAddress() + "\n");

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	protected void onPause() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (mBluetoothServerSocket != null) {
				mBluetoothServerSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onPause();
	}

	public void run() {
		tv.append("Thread is running.\n");

		try {
			mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BTChatActivity_Server",
					UUID.fromString(MY_UUID));
			tv.append("mBluetoothServerSocket: " + mBluetoothServerSocket.toString() + "\n");

			socket = null;
			// Keep listening until exception occurs or a socket is returned.
			while (true) {
				try {
					socket = mBluetoothServerSocket.accept();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append(" connection accepted.\n");
						}
					});
					Log.i("BTChatServerActivity", " connection accepted.");

					if (socket != null) {
						// A connection was accepted. Perform work associated
						// with
						// the connection in a separate thread.
						// manageMyConnectedSocket(socket);
						InputStream is = socket.getInputStream();
						OutputStream os = socket.getOutputStream();
						while (isRunning) {
							final int data = is.read();
							os.write(data);
							// Log.i("BTChatServerActivity", "" + (char) data);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									tv.append("" + (char) data);
								}
							});
						}

						// mBluetoothServerSocket.close();

					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
