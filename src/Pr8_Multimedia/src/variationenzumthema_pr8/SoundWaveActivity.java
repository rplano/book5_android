package variationenzumthema_pr8;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SoundWaveActivity
 *
 * This activity visualizes the effect of a hand moving in front of a speaker.
 * Can be used to detect gestures.
 *
 * @see SoundWave,
 *      https://www.microsoft.com/en-us/research/project/soundwave-using-the-doppler-effect-to-sense-gestures/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SoundWaveActivity extends Activity {

	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000
	private Player player;
	private volatile boolean playerIsRunning = true;
	private double frequency = 440;
	private boolean isPlaying = false;
	private boolean isRecording = true;

	private GraphView2 gv1;
	private GraphView2 gv2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sound_wave_activity);

		final SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
		seekBar.setProgress(4);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				frequency = progress * 100;
			}
		});

		final Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start playing sound
				if (isPlaying) {
					isPlaying = false;
					btn.setText("Start");
				} else {
					isPlaying = true;
					btn.setText("Stop");
				}
			}
		});

		// draw the two GraphViews side by side
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		int halfScreenWidth = (int) (screenWidth * 0.5);

		Spec row2 = GridLayout.spec(0);
		Spec col0 = GridLayout.spec(0);
		Spec col1 = GridLayout.spec(1);

		gv1 = (GraphView2) findViewById(R.id.graphview1);
		GridLayout.LayoutParams second = new GridLayout.LayoutParams(row2, col0);
		second.width = halfScreenWidth;
		gv1.setLayoutParams(second);
		gv1.setMin(Short.MIN_VALUE);
		gv1.setMax(Short.MAX_VALUE);
		gv1.setStyle(GraphView2.GraphStyle.LINE);
		gv1.setColor(Color.RED);
		gv1.setStrokeWidth(2);

		gv2 = (GraphView2) findViewById(R.id.graphview2);
		GridLayout.LayoutParams third = new GridLayout.LayoutParams(row2, col1);
		third.width = halfScreenWidth;
		gv2.setLayoutParams(third);
		gv2.setMin(Short.MIN_VALUE);
		gv2.setMax(Short.MAX_VALUE);
		gv2.setStyle(GraphView2.GraphStyle.LINE);
		gv2.setColor(Color.BLUE);
		gv2.setStrokeWidth(2);

		// start player:
		player = new Player();
		new Thread(player).start();

		// start recording thread:
		Recorder recorder = new Recorder();
		new Thread(recorder).start();
	}

	@Override
	protected void onPause() {
		isPlaying = false;
		playerIsRunning = false;

		isRecording = false;
		super.onPause();
	}

	class Recorder implements Runnable {
		private int bufferSize;
		private FFT fft;

		public Recorder() {
		}

		@Override
		public void run() {
			try {
				bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				bufferSize = nextpow2(bufferSize); // for FFT to work
				fft = new FFT(bufferSize);

				short[] data = new short[bufferSize];
				AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
						AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

				audioRecord.startRecording();

				while (isRecording) {
					int length = audioRecord.read(data, 0, bufferSize);

					// draw loudness in gv1
					for (int i = 0; i < data.length; i++) {
						gv1.addDataPoint(data[i]);
					}
					gv1.postInvalidate();

					// draw Fourier in gv2
					double[] magn = fft.doSimpleFFT(data);

					gv2.reset();
					gv2.setMax(fft.getMax());
					gv2.setMin(fft.getMin());
					int len = Math.min(gv2.getSize(), magn.length);
					for (int j = 0; j < len; j++) {
						gv2.addDataPoint(magn[j]);
					}
					// move to the left
					int delta = gv2.getSize() - len;
					for (int j = 0; j < delta - 10; j++) {
						gv2.addDataPoint(0);
					}
					gv2.postInvalidate();
				}

				audioRecord.stop();
				audioRecord.release();

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

	class Player implements Runnable {

		private AudioTrack audioTrack;
		private short[] data;
		private int bufferSize = 0;

		public Player() {
		}

		@Override
		public void run() {
			bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, 2 * bufferSize, AudioTrack.MODE_STREAM);
			audioTrack.flush();

			int step = 0;
			while (playerIsRunning) {
				if (isPlaying) {
					generateSound(step++);

					audioTrack.write(data, 0, data.length);
					if (step == 1) {
						audioTrack.play();
					}
				}
				// frequency = getPitch(49 + step);
			}

			audioTrack.stop();
			audioTrack.release();
		}

		private void generateSound(int step) {
			data = new short[bufferSize];
			double factor = 2 * Math.PI / (SAMPLE_RATE / frequency);
			for (int i = 0; i < bufferSize; ++i) {
				data[i] = (short) (Short.MAX_VALUE * Math.sin(factor * (i + step * bufferSize)));
			}
		}
	}
}
