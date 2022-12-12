package variationenzumthema_st3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MusicReceiver
 *
 * This BroadcastReceiver shows how to start the music service from a BroadcastReceiver.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MusicReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent musicServiceIntent = new Intent(context, MusicService.class);
		context.startService(musicServiceIntent);
	}
}
