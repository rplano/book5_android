package variationenzumthema_pr7;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BLETurnOnBTActivity
 *
 * This activity demonstrates how to turn bluetooth low energy and scan for
 * available devices. For it to work you must have the ACCESS_COARSE_LOCATION or
 * ACCESS_FINE_LOCATION permission.
 *
 * @see https://developer.android.com/guide/topics/connectivity/bluetooth-le
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BLETurnOnBTActivity extends Activity {

	private final int REQUEST_ENABLE_BT = 42;

	private TextView tv;
	private BluetoothAdapter mBluetoothAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		tv = (TextView) findViewById(R.id.textview);

		// Use this check to determine whether BLE is supported on the device.
		// Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
			finish();
		}
		tv.setText("BLE is supported.\n");

		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		tv.append("Bluetooth adapter initialized.\n");

		// Ensures Bluetooth is available on the device and it is enabled. If
		// not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			tv.append("Activity started For Result.\n");
		} else {
			tv.append("Could not start intent.\n");
		}

		tv.append("Done.\n");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String msg = "requestCode=" + requestCode + ", resultCode=" + resultCode;
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		tv.append(msg + "\n");
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			tv.append("Everything is fine!\n");

			// Device scan callback.
			BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
				@Override
				public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
					// runOnUiThread(new Runnable() {
					// @Override
					// public void run() {
					String tmp = "" + device.getAddress() + "," + device.getBluetoothClass() + ","
							+ device.getBondState() + "," + device.getName() + "," + device.getType();
					Log.i("BLEScannerActivity", tmp);
					// }
					// });
				}
			};

			mBluetoothAdapter.startLeScan(mLeScanCallback);

		}
		super.onActivityResult(requestCode, resultCode, intent);
	}
}
