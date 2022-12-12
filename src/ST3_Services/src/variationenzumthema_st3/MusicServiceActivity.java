package variationenzumthema_st3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import variationenzumthema.st3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MusicServiceActivity
 *
 * This activity allows you to start and stop the music service.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MusicServiceActivity extends Activity {

	private Intent musicServiceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_service_activity);

		musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);

		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startService(musicServiceIntent);
			}
		});

		Button btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(musicServiceIntent);
			}
		});
	}
}