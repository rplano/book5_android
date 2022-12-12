package variationenzumthema_st3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SimpleReceiver
 *
 * This BroadcastReceiver shows how to implement a simple BroadcastReceiver.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SimpleReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
		Toast.makeText(context, "Power disconnected,\nHi from SimpleReceiver!", Toast.LENGTH_SHORT).show();
	}
}
