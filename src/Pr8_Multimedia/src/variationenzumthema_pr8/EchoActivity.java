package variationenzumthema_pr8;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import variationenzumthema.pr8.R;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * EchoActivity
 *
 * This activity uses a BlockingQueue to create a little echo in replaying the
 * audio signal from the microphone.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EchoActivity extends Activity {

	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private int bufferSizeInByte = 0;
	private int bufferFactor = 5;

	private Player player;
	private Recorder recorder;

	private BlockingQueue<short[]> queue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.echo_activity);

		final SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
		seekBar.setProgress(bufferFactor);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				bufferFactor = progress;
			}
		});

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				seekBar.setEnabled(false);

				int minBufferSizeInByte = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				bufferSizeInByte = (minBufferSizeInByte * (bufferFactor + 2));

				// create shared queue
				queue = new ArrayBlockingQueue<short[]>(10);

				// start playing thread
				player = new Player(queue, SAMPLE_RATE, bufferSizeInByte);
				new Thread(player).start();

				// start recording thread
				recorder = new Recorder(queue, SAMPLE_RATE, bufferSizeInByte);
				new Thread(recorder).start();
			}
		});
	}

	@Override
	protected void onPause() {
		player.isPlaying = false;
		recorder.isRecording = false;
		super.onPause();
	}
}
