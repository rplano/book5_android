package variationenzumthema_pr6;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr6.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AlarmClockActivity
 *
 * This activity uses an AsyncTask to implement an alarm clock.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AlarmClockActivity extends Activity {

	private final int DELAY = 500;

	private TextView face;
	private EditText etAlarm;

	private AlarmClockTask srt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_clock_activity);

		face = (TextView) findViewById(R.id.textView);
		etAlarm = (EditText) findViewById(R.id.editText);

		Button btnStart = (Button) findViewById(R.id.button);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String time = etAlarm.getText().toString();
				int deltaTime = convertTimeInSeconds(time);
				long alarmTime = System.currentTimeMillis() + deltaTime;

				srt = new AlarmClockTask();
				srt.execute(alarmTime);
			}
		});
	}

	/**
	 * converts a time given in hours:minutes:seconds into seconds
	 *
	 * @param time
	 * @return
	 */
	private int convertTimeInSeconds(String time) {
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		if (time.length() == 0) {
			time = "0";
		}
		String[] times = time.split(":");

		if (times.length == 1) {
			hours = 0;
			minutes = 0;
			seconds = Integer.parseInt(times[0]);
		} else if (times.length == 2) {
			hours = 0;
			minutes = Integer.parseInt(times[0]);
			seconds = Integer.parseInt(times[1]);
		} else if (times.length == 3) {
			hours = Integer.parseInt(times[0]);
			minutes = Integer.parseInt(times[1]);
			seconds = Integer.parseInt(times[2]);
		} else {
			Log.e("AlarmClockActivity", "we should never get here!");
		}

		int deltaTime = hours * 3600000 + minutes * 60000 + seconds * 1000;
		return deltaTime;
	}

	/**
	 * converts a time given in seconds into a time given in
	 * hours:minutes:seconds
	 *
	 * @param time
	 * @return
	 */
	private String convertSecondsInTime(long time) {
		long hours = time / 3600000;
		long minutes = (time % 3600000) / 60000;
		long seconds = ((time % 3600000) % 60000) / 1000;
		String text = "";
		text += hours;
		text += ":";
		text += padWithChars("" + minutes, '0', 2);
		text += ":";
		text += padWithChars("" + seconds, '0', 2);
		return text;
	}

	private String padWithChars(String s, char c, int length) {
		while (s.length() < length) {
			s = c + s;
		}
		return s;
	}

	// Params, Progress, Result
	private class AlarmClockTask extends AsyncTask<Long, Long, String> {

		@Override
		protected void onProgressUpdate(Long... values) {
			face.setText(convertSecondsInTime(values[0]));
		}

		@Override
		protected String doInBackground(Long... params) {
			long alarmTime = params[0];

			while (true) {
				long remainingTime = alarmTime - System.currentTimeMillis();
				if (remainingTime <= 0)
					break;
				publishProgress(remainingTime);
				pause(DELAY);
			}

			return "done";
		}

		@Override
		protected void onPostExecute(String result) {
			// 100% volume
			ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
			// 1000 ms
			toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);
		}

		private void pause(int time) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
			}
		}
	}

}
