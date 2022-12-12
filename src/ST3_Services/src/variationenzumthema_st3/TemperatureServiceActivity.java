package variationenzumthema_st3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.st3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TemperatureServiceActivity
 *
 * This activity schedules the temperature service via the
 * JOB_SCHEDULER_SERVICE.
 * 
 * Nougat and later devices are not able to schedule a job if its
 * REFRESH_INTERVAL is less than 15 minutes. Good for battery life.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TemperatureServiceActivity extends Activity {
	private final String TEMPERATURE_FILE_NAME = "temperatures.txt";
	private final int JOB_ID = 42;
	private final int REFRESH_INTERVAL = 15 * 60 * 1000; // 15 minutes is
															// minimum time on
															// Nougat and later
															// devices!

	private JobScheduler mJobScheduler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperature_activity);

		mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

		readTemperaturesFromFile();

		Button btnStartJob = (Button) findViewById(R.id.btnStartJob);
		btnStartJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ComponentName componentName = new ComponentName(getApplicationContext(), TemperatureService.class);
				JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID, componentName);
				jobBuilder.setPeriodic(REFRESH_INTERVAL);
				// jobBuilder.setExtras(bundle).build();
				JobInfo jobInfo = jobBuilder.build();

				if (mJobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS) {
					Toast.makeText(v.getContext(), "TemperatureService is scheduled to execute every 15 minutes!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(v.getContext(), "TemperatureService could not be scheduled!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		Button btnStopJob = (Button) findViewById(R.id.btnStopJob);
		btnStopJob.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mJobScheduler.cancelAll();
			}
		});

		Button btnReadFile = (Button) findViewById(R.id.btnDeleteFile);
		btnReadFile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteFile(TEMPERATURE_FILE_NAME);
				readTemperaturesFromFile();
			}
		});

	}

	private void readTemperaturesFromFile() {
		try {
			FileInputStream fis = openFileInput(TEMPERATURE_FILE_NAME);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String temps = "";
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				temps = line + "\n" + temps;
			}
			br.close();
			fis.close();

			TextView tv = (TextView) findViewById(R.id.textview);
			tv.setMovementMethod(new ScrollingMovementMethod());
			tv.setText(temps);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
