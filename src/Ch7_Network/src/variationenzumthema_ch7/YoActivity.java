package variationenzumthema_ch7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * YoActivity
 *
 * This activity sends a simple UDP broadcast.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class YoActivity extends Activity implements Runnable {
	private final int YO_PORT = 3234;
	// max is 65507, 512 is the smallest size guaranteed by UDP
	private final int MAX_PACKET_SIZE = 512;

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yo_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);
		tv.setText("Saying 'Yo' to everyone: \n");
		tv.append("Broadcast to: " + Util.getLocalBroadcastAddress().getHostAddress() + "\n");
		sendYoToEveryone();

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendYoToEveryone();
			}
		});

		Thread th = new Thread(this);
		th.start();
	}

	private void sendYoToEveryone() {
		try {
			byte[] data = "Yo".getBytes();
			DatagramPacket theOutput = new DatagramPacket(data, data.length, Util.getLocalBroadcastAddress(), YO_PORT);
			DatagramSocket theSocket = new DatagramSocket();
			theSocket.send(theOutput);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			byte[] buffer = new byte[MAX_PACKET_SIZE];
			DatagramSocket server = new DatagramSocket(YO_PORT);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			Log.i("YoActivity", "UDPServer started, waiting for connections...");
			while (true) {
				try {
					server.receive(packet);
					String yoMessage = new String(packet.getData(), 0, packet.getLength());
					final String msg = "" + packet.getAddress() + ": " + yoMessage;

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("" + msg + "\n");
						}
					});

					// reset the length for the next packet
					packet.setLength(buffer.length);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}
		Log.i("YoActivity", "UDPServer finished.");
	}
}
