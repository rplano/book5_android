package variationenzumthema_ch6;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TimerActivity
 *
 * This activity shows how to run a simple non-UI task in the background with
 * the TimerTask.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TimerActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		long delay = 2000; // delay in ms before task is executed
		long period = 1000; // time in ms between successive executions
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.i("TimerActivity", "run()");
			}
		}, delay, period);

		pause(20000); // wait 10 secs

		timer.cancel();

		// finish();
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
