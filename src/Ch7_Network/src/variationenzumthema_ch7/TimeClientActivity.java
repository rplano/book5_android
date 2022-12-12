package variationenzumthema_ch7;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TimeClientActivity
 *
 * This activity implements a client that downloads date and time from the
 * locally running TimeServerActivity. 
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TimeClientActivity extends Activity {
	private final int PORT_TIME = 3737;
	private final int TIMEOUT = 1000; // network timeout in ms

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_client_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);

		final EditText et = (EditText) findViewById(R.id.edittext);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String IP = et.getText().toString();
				tv.setText("Local Time: " + new Date() + "\n");
				tv.append("Server Time: " + getTimeFromServer(IP) + "\n");
			}
		});
	}

	private String getTimeFromServer(String IP) {
		String daytime = "";
		try {
			SocketAddress sockaddr = new InetSocketAddress(IP, PORT_TIME);
			Socket socket = new Socket();
			socket.connect(sockaddr, TIMEOUT);

			InputStream is = socket.getInputStream();
			while (true) {
				int len = is.read();
				if (len == -1)
					break;
				daytime += (char) len;
			}

			is.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return daytime;
	}
}
