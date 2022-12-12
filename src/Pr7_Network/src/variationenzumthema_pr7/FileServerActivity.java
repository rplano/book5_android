package variationenzumthema_pr7;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * FileServerActivity
 *
 * This activity runs a local server that enables file downloads from the device
 * via a browser.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FileServerActivity extends Activity implements Runnable {
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
				Log.i("FileServerActivity", "#" + threadNr + ": " + socket.getInetAddress().getHostAddress());
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
				byte[] rawData;

				// what do you want?
				InputStream in = socket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String request = br.readLine(); // "GET /filename HTTP/1.1"
				String[] parts = request.split(" ");
				String whatDoYouWant = parts[1];
				Log.i("FileServerActivity", "request: " + whatDoYouWant);

				File dir = android.os.Environment.getExternalStorageDirectory();
				final File downloadFile = new File(dir, whatDoYouWant);
				if (!"/".equals(whatDoYouWant) && downloadFile.exists()) {
					rawData = getRawData(downloadFile);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv.append("#" + threadNr + ": " + downloadFile.getName() + "\n");
						}
					});
				} else {
					String content = getDirectoryContent(dir);
					String header = createHTTPHeader(content.length(), "text/html");
					rawData = (header + content).getBytes();
				}

				// here it is:
				OutputStream out = socket.getOutputStream();
				out.write(rawData);
				out.flush();

				out.close();
				in.close();
				socket.close();
				socket = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private byte[] getRawData(File downloadFile) {
			String mimeType = "application/octet-stream"; // "text/html"
			if (downloadFile.getAbsolutePath().endsWith(".png")) {
				mimeType = "image/png";
			}

			byte[] data = new byte[(int) downloadFile.length()];
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				FileInputStream fis = new FileInputStream(downloadFile);
				int read = 0;
				while ((read = fis.read(data)) != -1) {
					baos.write(data, 0, read);
				}
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String header = createHTTPHeader(downloadFile.length(), mimeType);
			byte[] head = header.getBytes();

			byte[] buffer = new byte[header.length() + (int) downloadFile.length()];
			System.arraycopy(head, 0, buffer, 0, head.length);
			System.arraycopy(data, 0, buffer, head.length, data.length);

			return buffer;
		}

		private String getDirectoryContent(File dir) {
			String content = "<html><body>";
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					content += "<a href='" + file.getName() + "'>" + file.getName() + "</a><br/>";
				}
			}
			content += "</body></html>";
			return content;
		}

		private String createHTTPHeader(long contentLength, String mimeType) {
			String httpHeader = "HTTP/1.0 200 OK\r\n" + "Server: FileServerActivity 1.0\r\n" + "Content-length: "
					+ contentLength + "\r\n" + "Content-type: " + mimeType + "\r\n\r\n";
			return httpHeader;
		}
	}
}
