package variationenzumthema_pr8;

import acm_graphics.*;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * FlappyBallActivity
 * 
 * Inspired by its big cousin. ToDo: dont touch floor!
 * 
 * Works best with headset or in a very quiet room. Given proper distances, one
 * could actually learn how to whistle tunes through an app like this ones.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FlappyBallActivity extends GraphicsProgram {

	// constants
	private final int MAX_FREQUENCY = 2000;
	private final int MIN_FREQUENCY = 400;
	private final int LOW_PASS_FACTOR = 20;

	private final int DELAY = 20;

	private final int WALL_WIDTH = 60;

	private final int BALL_DIAM = 60;
	private final int BALL_OFFSET = 4;

	// instance variables
	private RandomGenerator rgen = new RandomGenerator();

	private boolean alive = true;
	private int wallCounter = 0;
	// private double ballVel = 0.0;
	private double wallVel = 1.5;
	private GOval ball;
	private GRect upperWall;
	private GRect lowerWall;

	private double pitch = MIN_FREQUENCY;

	public void run() {
		waitForTouch();
		setup();

		// start recording thread
		Recorder recorder = new Recorder();
		new Thread(recorder).start();

		while (alive) {

			Log.i("FlappyBallActivity", "pitch=" + pitch);

			moveBall();
			moveWall();
			checkForCollision();

			pause(DELAY);
		}

		// println("Game over! You survived " + wallCounter + " walls.");
	}

	@Override
	protected void onPause() {
		alive = false;
		super.onPause();
	}

	double pitchAvg = MIN_FREQUENCY;

	/** Update and move ball */
	private void moveBall() {
		if (pitch > MIN_FREQUENCY && pitch < MAX_FREQUENCY) {
			pitchAvg = (LOW_PASS_FACTOR * pitchAvg + pitch) / (LOW_PASS_FACTOR + 1);
			double y = getHeight() - BALL_DIAM
					- ((pitchAvg - MIN_FREQUENCY) / (MAX_FREQUENCY - MIN_FREQUENCY)) * getHeight();
			Log.i("FlappyBallActivity", "y=" + y);

			ball.setLocation(getWidth() / 2, (int) y);
		}
	}

	private void moveWall() {
		upperWall.move((int) -wallVel, 0);
		lowerWall.move((int) -wallVel, 0);
		if (upperWall.getX() < -BALL_DIAM) {
			remove(upperWall);
			remove(lowerWall);
			createNewWall();
			wallCounter++;
			wallVel += 0.3;
		}
	}

	// // public void keyTyped(KeyEvent e) {
	// public void mousePressed(int x, int y) {
	// ballVel = BALL_ACCEL;
	// }

	private void checkForCollision() {
		// checkForCollisionWithFloor();
		checkForCollisionWithWall();
	}

	private void checkForCollisionWithWall() {
		GObject obj = getElementAt(ball.getX() + BALL_DIAM, ball.getY());
		if ((obj == lowerWall) || (obj == upperWall)) {
			alive = false;
		}
	}

	// private void checkForCollisionWithFloor() {
	// if (ball.getY() >= getHeight() - BALL_DIAM - BALL_OFFSET) {
	// //ballVel = 0.0;
	// ball.setLocation((int) getWidth() / 2, getHeight() - BALL_DIAM -
	// BALL_OFFSET);
	// }
	// }

	/** Create and place ball. */
	private void setup() {

		ball = new GOval(BALL_DIAM, BALL_DIAM);
		ball.setFilled(true);
		ball.setFillColor(Color.RED);
		add(ball, getWidth() / 2, 100);

		createNewWall();

		// addKeyListeners();
	}

	private void createNewWall() {
		int middle = rgen.nextInt(2 * BALL_DIAM, getHeight() - 2 * BALL_DIAM);
		upperWall = new GRect(getWidth(), 0, WALL_WIDTH, middle - BALL_DIAM);
		upperWall.setFilled(true);
		upperWall.setFillColor(rgen.nextColor());
		add(upperWall);
		lowerWall = new GRect(getWidth(), middle + BALL_DIAM, WALL_WIDTH, getHeight());
		lowerWall.setFilled(true);
		lowerWall.setFillColor(rgen.nextColor());
		add(lowerWall);
	}

	class Recorder implements Runnable {
		// constants
		private final int SAMPLE_RATE = 8000; // 44100, 22050, 16000, 11025 and
		// 8000

		// instance variables
		private AudioRecord audioRecord;
		private int bufferSize = 0;

		private FFT fft;
		
		public Recorder() {
		}

		@Override
		public void run() {
			try {

				// Fourier needs power of two sized arrays
				bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				bufferSize = nextpow2(bufferSize);
				fft = new FFT(bufferSize);

				// prepare recording
				short[] data = new short[bufferSize];
				audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bufferSize);
				audioRecord.startRecording();

				// record
				while (alive) {
					int length = audioRecord.read(data, 0, bufferSize);

					double[] magn = fft.doSimpleFFT(data);

					double LOWEST_FREQUENCY = SAMPLE_RATE / (double) bufferSize;
					pitch = fft.getMaxK() * LOWEST_FREQUENCY;
				}

				audioRecord.stop();

			} catch (Exception e) {
				Log.i(getLocalClassName(), e.getMessage());
			}

		}

		private int nextpow2(int i) {
			int pow2 = 2;
			while (pow2 <= i) {
				pow2 *= 2;
			}
			return pow2;
		}
	}

}
