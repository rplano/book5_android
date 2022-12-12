package variationenzumthema_ch7;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SocketActivity
 *
 * This activity uses a TCP Socket to download a web page.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SocketActivity extends Activity {
	private final int TIMEOUT = 1000; // network timeout in ms

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		TextView tv = (TextView) findViewById(R.id.textview);

		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			
			SocketAddress sockaddr = new InetSocketAddress("www.google.com", 80);
			Socket socket = new Socket();
			socket.connect(sockaddr, TIMEOUT);
			Log.i("SocketActivity", ""+socket.getLocalAddress());
			Log.i("SocketActivity", ""+socket.getRemoteSocketAddress());
			
			// write to socket
			OutputStream os = socket.getOutputStream();
			os.write("GET / \r\n".getBytes());
			os.flush();
			
			// read from socket
			InputStream is = socket.getInputStream();
			//String msg = "";
			while (true) {
				int len = is.read();
				if (len == -1)
					break;
				//msg += (char) len;
				tv.append(""+(char) len);
			}		

			os.close();
			is.close();
			socket.close();
			
		} catch (Exception e) {
			Log.e("SocketActivity", "Exception", e);
		}
	}
}
