package variationenzumthema_pr6;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import variationenzumthema.pr6.R;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SpeedReaderActivity
 *
 * This activity shows how to use AsyncTask to access UI thread.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SpeedReaderActivity extends Activity {
	private final int MAX_SPEED = 1000;
	private final int INITIAL_SPEED = 500;

	private SpeedReaderTask srt;
	private TextView tv;
	private String text = "We hold these truths to be self-evident, " 
			+ "that all men are created equal, "
			+ "that they are endowed by their Creator with certain unalienable Rights, "
			+ "that among these are Life, Liberty and the pursuit of Happiness.";
	private int delay = 500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speedreader_activity);

		tv = (TextView) findViewById(R.id.text);

		SeekBar sb = (SeekBar) findViewById(R.id.seekbar);
		sb.setMax(MAX_SPEED);
		sb.setProgress(INITIAL_SPEED);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				delay = progress;
			}
		});

		ImageButton imgBtn = (ImageButton) findViewById(R.id.img_button);
		imgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (srt.getStatus() == Status.FINISHED) {
					srt = new SpeedReaderTask();
					srt.execute(text);
				}
			}
		});

		srt = new SpeedReaderTask();
		srt.execute(text);
	}

	// Params, Progress, Result
	private class SpeedReaderTask extends AsyncTask<String, String, String> {
		//private String[] words;

		@Override
		protected void onProgressUpdate(String... values) {
			String word = values[0];
			tv.setText(word);
		}

		@Override
		protected String doInBackground(String... params) {
			String text = params[0];
			String[] words = text.split(" ");

			for (String word : words) {
				try {
					Thread.sleep(delay);
					publishProgress(word);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return "done";
		}
	}
}