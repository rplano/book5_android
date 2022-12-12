package variationenzumthema_st3;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.st3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SimpleServiceActivity
 *
 * This activity shows how to start a simple service from an activity and test
 * if it is still running.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SimpleServiceActivity extends Activity {

	private Intent simpleServiceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_service_activity);

		simpleServiceIntent = new Intent(getApplicationContext(), SimpleService.class);

		final TextView tv = (TextView) findViewById(R.id.textView);

		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startService(simpleServiceIntent);
			}
		});

		Button btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(simpleServiceIntent);
			}
		});

		Button btnTest = (Button) findViewById(R.id.btnTest);
		btnTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv.append("running: " + isServiceRunning(SimpleService.class) + "\n");
			}
		});
	}

	private boolean isServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
