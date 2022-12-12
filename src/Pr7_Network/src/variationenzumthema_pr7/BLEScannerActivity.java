package variationenzumthema_pr7;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BLEScannerActivity
 *
 * This activity scans for bluetooth low energy devices.
 *
 * @see Android Things - Communicating with Bluetooth Low Energy devices,
 *      nilhcem.com/android-things/bluetooth-low-energy
 * @see Android BluetoothLeGatt Sample,
 *      https://github.com/googlesamples/android-BluetoothLeGatt
 * @see https://github.com/googlesamples/android-BluetoothAdvertisements/#readme
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BLEScannerActivity extends Activity {

	private final int DELAY = 30 * 1000;

	private TextView tv;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothAdapter.LeScanCallback mLeScanCallback;

	private Map<String, String> devices = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		tv = (TextView) findViewById(R.id.textview);

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		tv.append("Bluetooth adapter initialized.\n");

		// Device scan callback.
		mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
			@Override
			public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
				String tmp = "" + device.getAddress() + "," + device.getBluetoothClass() + "," + device.getBondState()
						+ "," + device.getName() + "," + rssi;
				Log.i("BLEScannerActivity", tmp);

				devices.put(device.getAddress(), tmp);
				String t = "";
				for (String name : devices.keySet()) {
					t += devices.get(name) + "\n";
				}
				tv.setText(t);
			}
		};

		mBluetoothAdapter.startLeScan(mLeScanCallback);

		tv.append("Done.\n");

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.i("TimerActivity", "run()");
				devices.clear();
			}
		}, DELAY, DELAY);
	}

	@Override
	protected void onPause() {
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		super.onPause();
	}
}
