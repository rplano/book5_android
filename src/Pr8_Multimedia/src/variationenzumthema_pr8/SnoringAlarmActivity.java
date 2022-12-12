package variationenzumthema_pr8;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SnoringAlarmActivity
 *
 * This activity gets started from the SnoringServiceActivity via an intent. It
 * simply plays an alarm sound.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SnoringAlarmActivity extends Activity {
	private MediaPlayer player;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snoring_activity);

		Button btnStart = (Button) findViewById(R.id.btnStop);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (player != null) {
					player.stop();
				}
			}
		});

		playAlarm(this);
	}

	@Override
	protected void onPause() {
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
			}
			player.release();
		}
		super.onPause();
	}

	private void playAlarm(Context context) {
		try {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

			player = new MediaPlayer();
			player.setDataSource(context, alert);
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.prepare();
			player.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
