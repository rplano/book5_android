package variationenzumthema_pr8;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import acm_graphics.GObject;
import acm_graphics.GRect;
import acm_graphics.GraphicsProgram;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Piano
 * 
 * A GraphicsProgram that simulates a piano, plays tunes based on mouse click.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PianoActivity extends GraphicsProgram {

	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private int bufferSize = 0;
	private BlockingQueue<short[]> queue;

	private final int HEIGHT_OFFSET = 1;

	private GRect[] keys = new GRect[12];
	private int[] pitches = { 5, 7, 9, 11, 12, 14, 16, 6, 8, 10, 13, 15 };

	public void run() {
		waitForTouch();
		setup();

		// start playing thread
		bufferSize = SAMPLE_RATE / 2;
		queue = new ArrayBlockingQueue<short[]>(10);
		Player player = new Player(queue, SAMPLE_RATE, bufferSize);
		new Thread(player).start();
	}

	// public void mouseClicked(MouseEvent e) {
	public void mousePressed(int x, int y) {
		// int x = e.getX();
		// int y = e.getY();
		GObject obj = getElementAt(x, y);
		if (obj != null) {
			for (int i = 0; i < keys.length; i++) {
				if (obj == keys[i]) {
					try {
						Log.i("PianoActivity", "pitches=" + pitches[i]);
						double pitch = getPitch(pitches[i] + 40);
						Log.i("PianoActivity", "pitch=" + pitch);

						short[] data = generateSound(pitch);
						queue.put(data);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private short[] generateSound(double pitch) {
		short[] data = new short[bufferSize];
		double factor = 2 * Math.PI / (SAMPLE_RATE / pitch);
		for (int i = 0; i < bufferSize; i++) {
			data[i] = (short) (Short.MAX_VALUE * Math.sin(factor * i));
		}
		return data;
	}

	/**
	 * A4 (the reference pitch) is the 49th key from the left end of a piano
	 */
	private double getPitch(int key) {
		// P40 = 440(12√2)(40 − 49) ≈ 261.626 Hz
		double base = Math.pow(2.0, 1.0 / 12.0);
		double pitch = 440.0 * Math.pow(base, (key - 49.0));
		return pitch;
	}

	private void setup() {
		// setSize(WIDTH, HEIGHT);
		addMouseListeners();

		int keyCounter = 0;

		// draw 8 white keys
		int width = getWidth();
		int height = getHeight();
		for (int i = 0; i < 7; i++) {
			keys[keyCounter] = new GRect(width / 7, height - HEIGHT_OFFSET);
			add(keys[keyCounter], i * width / 7, 0);
			keyCounter++;
		}

		// draw 3 + 2 black keys
		for (int i = 0; i < 3; i++) {
			keys[keyCounter] = new GRect(width / 12, height / 2);
			keys[keyCounter].setFilled(true);
			add(keys[keyCounter], i * width / 7 + width / 7 / 2 + 20, 0);
			keyCounter++;
		}
		for (int i = 0; i < 2; i++) {
			keys[keyCounter] = new GRect(width / 12, height / 2);
			keys[keyCounter].setFilled(true);
			add(keys[keyCounter], i * width / 7 + 9 * width / 7 / 2 + 20, 0);
			keyCounter++;
		}
	}

	// class Player implements Runnable {
	// private final BlockingQueue<short[]> queue;
	//
	// public Player(BlockingQueue<short[]> q) {
	// queue = q;
	// }
	//
	// public void run() {
	// try {
	// // Log.i(getLocalClassName(), "received audio: " + data.length);
	// AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
	// SAMPLE_RATE,
	// AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize,
	// AudioTrack.MODE_STREAM);
	//
	// audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
	// AudioFormat.CHANNEL_OUT_MONO,
	// AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
	// audioTrack.flush();
	// audioTrack.play();
	// while (true) {
	// short[] data = queue.take();
	// audioTrack.write(data, 0, data.length);
	// }
	//
	// //audioTrack.release();
	// //audioTrack.stop();
	//
	// } catch (InterruptedException e) {
	// Log.i(getLocalClassName(), e.getMessage());
	// }
	// }
	// }
}