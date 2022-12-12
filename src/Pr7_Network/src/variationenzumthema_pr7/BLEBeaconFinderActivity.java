package variationenzumthema_pr7;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
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
 * BLEBeaconFinderActivity
 *
 * This activity scans for a particular bluetooth LE beacon and continually
 * monitors its signal strength.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BLEBeaconFinderActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothAdapter.LeScanCallback mLeScanCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beacon_finder_activity);

		final TextView tv = (TextView) findViewById(R.id.textView);
		final EditText et = (EditText) findViewById(R.id.editText);
		et.setText("FF:FF:40:00:7B:EA");

		mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
			@Override
			public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
				String mac = et.getText().toString().trim();
				if (mac.equals(device.getAddress().toUpperCase())) {
					String tmp = "" + device.getAddress() + ": " + rssi + "db";
					tv.setText(tmp + "\n" + tv.getText());
				}
			}
		};

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String mac = et.getText().toString().trim();
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
		});

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			tv.setText("Please turn on Bluetooth.\n");
		} else {
			tv.append("Bluetooth initialized.\n");
		}
	}

	@Override
	protected void onPause() {
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		super.onPause();
	}
}
