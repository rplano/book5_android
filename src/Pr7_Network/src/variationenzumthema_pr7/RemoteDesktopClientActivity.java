package variationenzumthema_pr7;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import acm_graphics.GImage;
import acm_graphics.GraphicsProgram;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RemoteDesktopClientActivity
 *
 * This activity allows you to remote control a desktop computer. It needs the
 * RemoteDesktopServer to run on the desktop computer that you want to remote
 * control.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RemoteDesktopClientActivity extends GraphicsProgram {
	private final int PORT = 1778;
	private final String IP = "192.168.178.54";
	private final int TIMEOUT = 1000;
	private final double SCALE = 1.0;

	private GImage img;
	private int oldX, oldY;

	enum COMMAND {
		GET_SCREENSHOT, MOUSE_MOVE, MOUSE_CLICKED
	}

	@Override
	public void init() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		addMouseListeners();
	}

	public void run() {
		waitForTouch();
		while (true) {
			sendRequestToServer(COMMAND.GET_SCREENSHOT, 0, 0);
			if (img != null) {
				img.scale(SCALE);
				add(img);
			}
			pause(500);
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		// System.out.println("mouseMoved x=" + x + ", y=" + y);
		x = (int) (x / SCALE);
		y = (int) (y / SCALE);
		sendRequestToServer(COMMAND.MOUSE_MOVE, x, y);
	}

	@Override
	public void mousePressed(int x, int y) {
		oldX = (int) (x / SCALE);
		oldY = (int) (y / SCALE);
	}

	@Override
	public void mouseReleased(int x, int y) {
		x = (int) (x / SCALE);
		y = (int) (y / SCALE);
		int dx = oldX - x;
		int dy = oldY - y;
		// moved at less than 4 pixel
		if (dx * dx + dy * dy <= 16) {
			sendRequestToServer(COMMAND.MOUSE_CLICKED, x, y);
		}
	}

	private void sendRequestToServer(COMMAND cmd, int x, int y) {
		try {
			SocketAddress sockaddr = new InetSocketAddress(IP, PORT);
			Socket socket = new Socket();
			socket.connect(sockaddr, TIMEOUT);
			// System.out.println("connection established.");

			OutputStream out = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(out);
			dos.writeInt(cmd.ordinal());

			switch (cmd) {

			case GET_SCREENSHOT:
				getScreenShotFromServer(socket);
				break;

			case MOUSE_MOVE:
				dos.writeInt(x);
				dos.writeInt(y);
				break;

			case MOUSE_CLICKED:
				dos.writeInt(x);
				dos.writeInt(y);
				break;

			default:
				System.out.println("Unknown command.");
				break;
			}

			out.close();
			socket.close();
			// System.out.println("Connection closed.\n");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void getScreenShotFromServer(Socket socket) {
		Bitmap image = null;
		try {
			InputStream is = socket.getInputStream();
			// image = ImageIO.read(is);
			image = BitmapFactory.decodeStream(is);
			is.close();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (image != null) {
			if (img != null) {
				// allow garbage collector to do its work
				remove(img);
				img = null;
			}
			img = new GImage(image);
		}
	}

}
