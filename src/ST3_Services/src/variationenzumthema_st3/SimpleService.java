package variationenzumthema_st3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SimpleService
 *
 * This service demonstrates how a simple service is implemented.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SimpleService extends Service {

	@Override
	public void onCreate() {
		Log.i("SimpleService", "onCreate()");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("SimpleService", "onStartCommand()");

		// if we get killed, restart
		// return START_STICKY;

		// Don't automatically restart this Service if it is killed
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i("SimpleService", "onDestroy()");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
