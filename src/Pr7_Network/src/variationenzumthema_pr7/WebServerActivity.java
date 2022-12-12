package variationenzumthema_pr7;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import variationenzumthema.pr7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WebServerActivity
 *
 * This activity runs a simple web server on the local network. Try to run this
 * once with wifi turned on and once with cellular network turned on. Also, try
 * to test it with Chrome and with Firefox.
 * 
 * @see Scan_wifi_connections_or_Get_number_of_available_wifi_connections
 *      /index.php?view=article_discription&aid=90&aaid=114
 * @see What to do with chrome sending extra requests?,
 *      https://stackoverflow.com/questions/4460661/what-to-do-with-chrome-sending-extra-requests
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WebServerActivity extends Activity implements Runnable {
	private final int PORT = 8008;

	private boolean isRunning = true;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		tv = (TextView) findViewById(R.id.textview);
		tv.setTextIsSelectable(true);
		tv.setText("My IP: " + Util.getMyLocalIpAddress().getHostAddress() + ":" + PORT + "\n");

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		try {
			int threadNr = 0;
			ServerSocket server = new ServerSocket(PORT);
			while (isRunning) {
				Socket socket = server.accept();
				final String msg = "#" + threadNr + ": " + socket.getInetAddress().getHostAddress();
				Log.i("WebServerActivity", msg);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv.append("" + msg + "\n");
					}
				});
				(new ConnectionThread(++threadNr, socket)).start();
			}
			server.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		isRunning = false;
		super.onDestroy();
	}

	class ConnectionThread extends Thread {
		private int threadNr;
		private Socket socket;

		public ConnectionThread(int threadNr, Socket socket) {
			this.threadNr = threadNr;
			this.socket = socket;
		}

		public void run() {
			try {
				OutputStream out = socket.getOutputStream();
				String http = createHTTPResponse();
				out.write(http.getBytes());
				out.flush();
				out.close();
				socket.close();
				socket = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String createDummyHTML() {
			String html = "<html><body>" + "<h1>Hello from WebServerActivity!</h1>" + "<p>You are visitor number "
					+ threadNr + ".</p>" + "</body></html>";
			return html;
		}

		private String createHTTPHeader(long contentLength, String mimeType) {
			String httpHeader = "HTTP/1.0 200 OK\r\n" + "Server: WebServerActivity 1.0\r\n" + "Content-length: "
					+ contentLength + "\r\n" + "Content-type: " + mimeType + "\r\n\r\n";
			return httpHeader;
		}

		private String createHTTPResponse() {
			String html = createDummyHTML();
			String httpHeader = createHTTPHeader(html.length(), "text/html");
			return httpHeader + html;
		}
	}
}
