package variationenzumthema_pr8;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TuneGeneratorActivity
 *
 * This activity generates audio of a preset pitch, for tuning a musical
 * instrument, for instance.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TuneGeneratorActivity extends Activity implements Runnable {

	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000
	// private final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private AudioTrack audioTrack;
	private short[] data;
	private int bufferSize = 0;
	private double frequency = 440;
	private boolean isPlaying = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tune_generator_activity);

		final Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlaying) {
					isPlaying = false;
					btnStart.setText("Start");
				} else {
					isPlaying = true;
					btnStart.setText("Stop");
				}
			}
		});

		final TextView tvPitch = (TextView) findViewById(R.id.pitch);

		SeekBar seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				frequency = getPitch(progress);
				tvPitch.setText("" + (int) (Math.round(frequency)) + "Hz (" + getPitchName(progress) + ")");
			}
		});

		new Thread(this).start();
	}

	@Override
	public void run() {
		bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 2 * bufferSize, AudioTrack.MODE_STREAM);
		audioTrack.flush();

		int step = 0;
		while (true) {
			if (isPlaying) {
				generateSound(step++);

				audioTrack.write(data, 0, data.length);
				if (step == 1) {
					audioTrack.play();
				}
			}
			// frequency = getPitch(49 + step);
		}
	}

	@Override
	protected void onPause() {
		isPlaying = false;
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
		}
		super.onPause();
	}

	private void generateSound(int step) {
		data = new short[bufferSize];
		double factor = 2 * Math.PI / (SAMPLE_RATE / frequency);
		for (int i = 0; i < bufferSize; ++i) {
			data[i] = (short) (Short.MAX_VALUE * Math.sin(factor * (i + step * bufferSize)));
		}
	}

	/**
	 * A4 (the reference pitch) is the 49th key from the left end of a piano
	 */
	private double getPitch(int key) {
		// P40 = 440(12√2)(40 − 49) ≈ 261.626 Hz
		double base = Math.pow(2.0, 1.0 / 12.0);
		double pitch = 440.0 * Math.pow(base, (key - 49));
		return pitch;
	}

	private final String[] pitchNames = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

	private String getPitchName(int key) {
		String pitchName = pitchNames[(key + 8) % 12];
		pitchName += ((key + 8) / 12);
		return pitchName;
	}
}
