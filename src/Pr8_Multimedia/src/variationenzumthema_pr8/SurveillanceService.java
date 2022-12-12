package variationenzumthema_pr8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SurveillanceService
 *
 * This service takes a picture once every 10 seconds. It is started and stopped
 * through SurveillanceServiceActivity.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SurveillanceService extends Service {
	private Timer timer;
	private File picturesDir;

	@Override
	public void onCreate() {
		Log.i("SurveillanceService", "onCreate()");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("SurveillanceService", "onStartCommand()");

		picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"SurveillanceService");
		picturesDir.mkdirs();

		long delay = 0; // delay in ms before task is executed
		long period = 10 * 1000; // time in ms between successive executions

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.i("SurveillanceService", "taking image...");
				takePicture();
			}
		}, delay, period);

		// if we get killed, restart
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i("SurveillanceService", "onDestroy()");
		super.onDestroy();

		timer.cancel();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void takePicture() {
		try {
			SurfaceTexture surfaceTexture = new SurfaceTexture(42);

			Camera camera = Camera.open(0);
			camera.setPreviewTexture(surfaceTexture);

			Camera.Parameters params = camera.getParameters();
			params.setPreviewSize(640, 480); // this is for preview
			params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			params.setPictureSize(640, 480); // this is for actual picture
												// taken!
			params.setPictureFormat(ImageFormat.JPEG);
			camera.setParameters(params);

			camera.startPreview();
			camera.takePicture(null, null, new PictureCallback() {

				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					writeImageToFile(data);

					camera.setPreviewCallback(null);
					camera.stopPreview();
					camera.release();
					camera = null;
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeImageToFile(byte[] data) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			String date = dateFormat.format(new Date());

			String filename = picturesDir.getPath() + File.separator + "Image_" + date + ".jpg";
			FileOutputStream fos;
			fos = new FileOutputStream(new File(filename));
			fos.write(data);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
