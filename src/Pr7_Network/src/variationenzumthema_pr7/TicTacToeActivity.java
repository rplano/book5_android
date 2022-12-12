package variationenzumthema_pr7;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import acm_graphics.*;
import android.graphics.Color;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * TicTacToeActivity
 * 
 * An implementation of the TicTacToe game as a network game. It uses the
 * ConnectionBroker class to do this.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TicTacToeActivity extends GraphicsProgram {
	private final int OFFSET = 47;
	private int WIDTH = 600;
	private int HEIGHT = 600 + OFFSET;
	private int CELL_WIDTH = 200;
	private double SCALE = 1.0;
	private int currentPlayer = 1;
	private int me = 1;

	private boolean gameHasStarted = false;
	private ObjectOutputStream oos;

	public void init() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	public void run() {
		waitForTouch();
		setup();

		// establish connection to other player
		Log.i("TicTacToeActivity", "waiting...");
		ConnectionBroker broker = new ConnectionBroker();
		broker.start();
		while (!broker.isSocketAvailable()) {
			pause(200);
		}
		Log.i("TicTacToeActivity", "connection established.");
		showToastInUIThread("Game has started!");

		// who starts?
		if (broker.isServer()) {
			me = 2;
		}

		// now get InputStream and OutputStream
		final Socket socket = broker.getSocket();
		try {
			Log.i("TicTacToeActivity", "starting InConnectionThread...");
			InConnectionThread in = new InConnectionThread(socket.getInputStream());
			in.start();

			oos = new ObjectOutputStream(socket.getOutputStream());
			gameHasStarted = true;
			if (currentPlayer == me) {
				showToastInUIThread("Your move!");
			} else {
				showToastInUIThread("Wait for the other player to make the first move!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showToastInUIThread(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void mousePressed(int x, int y) {
		if (gameHasStarted) {
			if (currentPlayer == me) {
				int i = x / CELL_WIDTH;
				int j = y / CELL_WIDTH;

				if (TicTacToeLogic.isMoveAllowed(currentPlayer, i, j)) {
					displayPlayer(i, j, currentPlayer);
					sendMoveToOtherPlayer(i, j);
				}

				if (TicTacToeLogic.isGameOver()) {
					displayGameOver();
				}
			} else {
				Toast.makeText(getApplicationContext(), "It is not your turn!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Other player still needs to join!", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendMoveToOtherPlayer(int i, int j) {
		try {
			Point p = new Point(i, j);
			oos.writeObject(p);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void displayGameOver() {
		GLabel lbl = new GLabel("Player " + currentPlayer + " lost!");
		lbl.setColor(Color.RED);
		lbl.setFont("SansSerif-72");
		lbl.setLocation((WIDTH - lbl.getWidth()) / 2, HEIGHT / 2);
		add(lbl);
	}

	private void displayPlayer(int i, int j, int player2) {
		Log.i("TicTacToeActivity", "displayPlayer() " + player2);
		if (currentPlayer == 1) {
			GImage img = new GImage("TicTacToe_X.png", this);
			img.scale(SCALE);
			add(img, i * CELL_WIDTH, j * CELL_WIDTH);
			currentPlayer = 2;
		} else {
			GImage img = new GImage("TicTacToe_O.png", this);
			img.scale(SCALE);
			add(img, i * CELL_WIDTH, j * CELL_WIDTH);
			currentPlayer = 1;
		}
		this.invalidateView();
	}

	private void setup() {
		// setSize(WIDTH, HEIGHT);
		GImage background = new GImage("TicTacToe_background.png", this);
		double backgroundSize = background.getWidth();
		double screenSize = this.getWidth();
		SCALE = screenSize / backgroundSize;
		WIDTH = (int) (backgroundSize * SCALE);
		HEIGHT = WIDTH;
		CELL_WIDTH = (int) (CELL_WIDTH * SCALE);
		background.scale(SCALE);
		add(background);
		addMouseListeners();
	}

	class InConnectionThread extends Thread {
		private InputStream is;

		public InConnectionThread(InputStream is) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			this.is = is;
		}

		@Override
		public void run() {
			Log.i("InConnectionThread", "InConnectionThread running");
			try {
				ObjectInputStream ois = new ObjectInputStream(is);
				while (true) {
					Point p = (Point) ois.readObject();
					Log.i("InConnectionThread", "Point received: " + p);
					displayPlayer(p.x, p.y, currentPlayer);
				}
				// ois.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
