package variationenzumthema_st3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WhoStoleMyPhoneService
 *
 * This service takes a picture via a BroadcastReceiver whenever one of the
 * following action happens: ACTION_SCREEN_ON, ACTION_SCREEN_OFF or
 * ACTION_USER_PRESENT.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WhoStoleMyPhoneService extends Service {
	private BroadcastReceiver mReceiver;

	@Override
	public void onCreate() {
		Log.i("WhoStoleMyPhoneService", "onCreate()");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("WhoStoleMyPhoneService", "onStartCommand()");

		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_USER_PRESENT);
		mReceiver = new WhoStoleMyPhoneReceiver();
		registerReceiver(mReceiver, intentFilter);

		// if we get killed, restart
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i("WhoStoleMyPhoneService", "onDestroy()");
		super.onDestroy();

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class WhoStoleMyPhoneReceiver extends BroadcastReceiver {
		private Camera camera;
		private SurfaceTexture surfaceTexture;

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("WhoStoleMyPhoneReceiver.onReceive()", "" + intent.getAction());
			if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
				surfaceTexture = new SurfaceTexture(42);
				takePicture();
				Toast.makeText(context, "Smile!", Toast.LENGTH_SHORT).show();
			}
		}

		private void takePicture() {
			try {
				if (camera == null) {
					camera = Camera.open(1);
					camera.setPreviewTexture(surfaceTexture);

					Camera.Parameters params = camera.getParameters();
					params.setPreviewSize(640, 480); // this is for preview
					params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					params.setPictureSize(640, 480); // this is for actual
														// picture
														// taken!
					params.setPictureFormat(ImageFormat.JPEG);
					camera.setParameters(params);
				}

				camera.startPreview();
				camera.takePicture(null, null, new PictureCallback() {

					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
						try {
							OutputStream stream = new FileOutputStream("/sdcard/WhoStoleMyPhone.png");
							bitmap.compress(CompressFormat.PNG, 0, stream);
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

						camera.stopPreview();
						camera.release();
						camera = null;
					}

				});

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}