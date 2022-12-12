package variationenzumthema_pr8;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.widget.ImageView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Camera360Activity
 *
 * This activity uses the front- and back-facing cameras to create a 360 degree
 * view.  Almost.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Camera360Activity extends Activity implements Runnable {
	private final int TEXTURE_OBJECT_NAME = 42;

	private Camera camera;
	private SurfaceTexture surfaceTexture;

	private ImageView iv0;
	private ImageView iv1;
	private int cameraId = 0;
	private boolean isCameraBusy = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_360_activity);

		iv0 = (ImageView) this.findViewById(R.id.imageView0);
		iv1 = (ImageView) this.findViewById(R.id.imageView1);

		surfaceTexture = new SurfaceTexture(TEXTURE_OBJECT_NAME);

		Thread th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		while (true) {
			if (!isCameraBusy) {
				cameraId++;
				cameraId %= 2;
				isCameraBusy = true;
				takePicture();
			}
			pause(10);
		}
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	private void takePicture() {
		try {
			camera = Camera.open(cameraId);
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
				public void onPictureTaken(byte[] data, Camera camera2) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					if (cameraId == 0) {
						iv0.setImageBitmap(bitmap);
					} else {
						iv1.setImageBitmap(bitmap);
					}
					camera.stopPreview();
					camera.release();
					camera = null;
					isCameraBusy = false;
				}

			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
		}
		super.onPause();
	}
}
