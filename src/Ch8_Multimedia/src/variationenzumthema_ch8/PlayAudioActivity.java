package variationenzumthema_ch8;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import variationenzumthema.ch8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PlayAudioActivity
 *
 * This activity shows how to play audio using the MediaPlayer.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PlayAudioActivity extends Activity {

	private MediaPlayer player;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		player = MediaPlayer.create(this, R.raw.trap_beat);
		player.start();
	}

	@Override
	protected void onDestroy() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		super.onDestroy();
	}
}