package variationenzumthema_pr7;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * BTDiscoveryActivity
 *
 * This activity lists available bluetooth devices.
 *
 * @see https://code.tutsplus.com/tutorials/create-a-bluetooth-scanner-with-androids-bluetooth-api--cms-24084
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BTDiscoveryActivity extends Activity {

	// https://developer.android.com/reference/android/bluetooth/BluetoothClass.Device.Major
	private final Map<Integer, String> BT_DEVICES = new HashMap<Integer, String>();
	{
		String deviceTypes = "AUDIO_VIDEO, 1024,COMPUTER, 256,HEALTH, 2304,IMAGING, 1536,MISC, 0,NETWORKING, 768,PERIPHERAL, 1280,PHONE, 512,TOY, 2048,UNCATEGORIZED, 7936,WEARABLE, 1792";
		StringTokenizer st = new StringTokenizer(deviceTypes, ",");
		while (st.hasMoreTokens()) {
			String name = st.nextToken().trim();
			int id = Integer.parseInt(st.nextToken().trim());
			BT_DEVICES.put(id, name);
		}
	}

	// https://developer.android.com/reference/android/bluetooth/BluetoothClass.Service
	private final Map<Integer, String> BT_SERVICES = new HashMap<Integer, String>();
	{
		String serviceTypes = "AUDIO, 2097152,CAPTURE, 524288,INFORMATION, 8388608,LIMITED_DISCOVERABILITY,8192,NETWORKING, 131072,OBJECT_TRANSFER, 1048576,POSITIONING, 65536,RENDER, 262144,TELEPHONY, 4194304";
		StringTokenizer st = new StringTokenizer(serviceTypes, ",");
		while (st.hasMoreTokens()) {
			String name = st.nextToken().trim();
			int id = Integer.parseInt(st.nextToken().trim());
			BT_SERVICES.put(id, name);
		}
	}

	private final String[] BT_DEVICE_TYPES = { "DEVICE_TYPE_UNKNOWN", "DEVICE_TYPE_CLASSIC", "DEVICE_TYPE_LE",
			"DEVICE_TYPE_DUAL" };

	private TextView tv;
	private BluetoothAdapter btAdapter;
	private BroadcastReceiver btReciever;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("");

		btAdapter = BluetoothAdapter.getDefaultAdapter();

		if (btAdapter.isDiscovering()) {
			btAdapter.cancelDiscovery();
		}

		tv.append("Paired devices:\n");
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		for (BluetoothDevice device : pairedDevices) {
			final String tmp = "" + device.getAddress() + "," + device.getBluetoothClass() + "," + device.getBondState()
					+ "," + device.getName() + "," + BT_DEVICE_TYPES[device.getType()];
			tv.append(tmp + "\n");
		}
		tv.append("\n");

		tv.append("Near devices:\n");
		btReciever = new BluetoothReceiver();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(btReciever, filter);
		btAdapter.startDiscovery();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(btReciever);
		btAdapter.cancelDiscovery();
		super.onPause();
	}

	public String getBTServices(BluetoothClass bc) {
		String services = "";

		for (int serviceID : BT_SERVICES.keySet()) {
			if (bc.hasService(serviceID)) {
				services += BT_SERVICES.get(serviceID) + "|";
			}
		}
		return services;
	}

	class BluetoothReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

				BluetoothClass bc = device.getBluetoothClass();
				String mdc = BT_DEVICES.get(bc.getMajorDeviceClass());
				String services = getBTServices(bc);
				String sbc = "" + mdc + "," + services;

				final String tmp = "" + device.getAddress() + "," + sbc + "," + device.getBondState() + ","
						+ device.getName() + "," + BT_DEVICE_TYPES[device.getType()] + "," + rssi + "db";
				Log.i("BTDiscoveryActivity", tmp);
				tv.append(tmp + "\n");
			}
		}
	}
}
