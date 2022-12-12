package variationenzumthema_st3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NotificationReceiver
 *
 * This BroadcastReceiver starts the notification service.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent musicServiceIntent = new Intent(context, NotificationService.class);
		context.startService(musicServiceIntent);
	}
}
