package variationenzumthema_st3;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import variationenzumthema.st3.R;
import variationenzumthema_st3.StepCounterBinderService.StepBinder;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * StepCounterBinderServiceActivity
 *
 * This activity starts and stops the step counter service. In addition it also
 * connects to the service through a Binder to get access to the steps variable.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class StepCounterBinderServiceActivity extends Activity {

	private StepCounterBinderService mService;
	private ServiceConnection mConnection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_counter_binder_activity);

		mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName className, IBinder service) {
				StepBinder binder = (StepBinder) service;
				mService = binder.getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// do nothing
			}
		};

		final Intent stepCounterServiceIntent = new Intent(getApplicationContext(), StepCounterBinderService.class);

		Button btnStartJob = (Button) findViewById(R.id.btnStartJob);
		btnStartJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startService(stepCounterServiceIntent);
			}
		});

		Button btnStopJob = (Button) findViewById(R.id.btnStopJob);
		btnStopJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(stepCounterServiceIntent);
			}
		});

		Button btnReadSteps = (Button) findViewById(R.id.btnReadSteps);
		btnReadSteps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mService != null) {
					float steps = mService.getNumberOfSteps();
					Toast.makeText(v.getContext(), "steps: " + steps, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(v.getContext(), "StepCounterBinderService not bound!", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = new Intent(this, StepCounterBinderService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(mConnection);
	}
}
