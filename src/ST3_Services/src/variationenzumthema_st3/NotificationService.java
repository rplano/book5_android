package variationenzumthema_st3;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import variationenzumthema.st3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NotificationService
 *
 * This service starts playing music in a loop, i.e., will not stop by itself.
 * It also shows a notification, thus telling the user it is running. Through
 * this notification, the user can start the notification activity, which allows
 * the user to stop this service.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NotificationService extends Service {

	private final int NOTIFICATION_ID = 42;

	private MediaPlayer player;

	@Override
	public void onCreate() {
		super.onCreate();

		Intent notificationIntent = new Intent(this, NotificationActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		Notification notification = new Notification.Builder(this).setSmallIcon(R.drawable.notification_icon)
				.setContentTitle("Cool Music App").setContentText("Play some cool music...")
				.setContentIntent(pendingIntent).build();

		startForeground(NOTIFICATION_ID, notification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (player == null) {
			player = MediaPlayer.create(this, R.raw.trap_beat);
			player.setLooping(true);
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
