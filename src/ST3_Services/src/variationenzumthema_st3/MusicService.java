package variationenzumthema_st3;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import variationenzumthema.st3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MusicService
 *
 * This service plays music when it is started.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MusicService extends Service {

	private MediaPlayer player;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (player == null) {
			player = MediaPlayer.create(this, R.raw.trap_beat);
			//player.setLooping(true);
			player.start();

		} else {
			if (player.isPlaying()) {
				player.seekTo(0);
			} else {
				player.start();
			}
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		if (player != null) {
			player.stop();
			player.release();
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
