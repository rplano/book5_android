package variationenzumthema_pr7;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BTChatClientActivity
 *
 * This activity demonstrates how to connect to a bluetooth server.
 *
 * @see https://developer.android.com/guide/topics/connectivity/bluetooth#Profiles
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BTChatClientActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket socket;
	private OutputStream os;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bt_chat_client_activity);

		TextView tv = (TextView) findViewById(R.id.textView);
		tv.setText("");

		final EditText et = (EditText) findViewById(R.id.editText);

		Button btn = (Button) findViewById(R.id.btnSend);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (os != null) {
						String msg = et.getText().toString() + "\n";
						os.write(msg.getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		tv.append("mBluetoothAdapter: " + mBluetoothAdapter.getAddress() + "\n");

		BluetoothDevice mBluetoothDevice = findPairedBluetoothDevice("Moto");

		if (mBluetoothDevice != null) {
			tv.append("Paired with: " + mBluetoothDevice.getName() + "\n");
			try {
				socket = mBluetoothDevice
						.createRfcommSocketToServiceRecord(UUID.fromString(BTChatServerActivity.MY_UUID));
				socket.connect();
				os = socket.getOutputStream();
				os.write("Hi there!\n".getBytes());
				tv.append("Successfully connected!\n");

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			tv.append("No device found!\n");
		}
	}

	private BluetoothDevice findPairedBluetoothDevice(String name) {
		BluetoothDevice mBluetoothDevice = null;
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		for (BluetoothDevice device : pairedDevices) {
			if (device.getName().trim().startsWith(name)) {
				mBluetoothDevice = device;
			}
		}
		return mBluetoothDevice;
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
		super.onPause();
	}
}
