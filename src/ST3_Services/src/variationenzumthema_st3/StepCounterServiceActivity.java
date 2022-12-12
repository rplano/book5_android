package variationenzumthema_st3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import variationenzumthema.st3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * StepCounterServiceActivity
 *
 * This activity starts and stops the step counter service.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class StepCounterServiceActivity  extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_counter_activity);

		final Intent stepCounterServiceIntent = new Intent(getApplicationContext(), StepCounterService.class);

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
	}

}
