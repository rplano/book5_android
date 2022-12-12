package variationenzumthema_pr8;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TakePhotoActivity
 *
 * This activity shows how to take a photo with the camera.
 *
 * @see https://developer.android.com/reference/android/hardware/Camera
 * @see http://www.vogella.com/tutorials/AndroidCamera/article.html
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TakePhotoActivity extends Activity {
	private final int TEXTURE_OBJECT_NAME = 42;

	private Camera camera;
	private SurfaceTexture surfaceTexture;
	private ImageView iv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_photo_activity);

		iv = (ImageView) this.findViewById(R.id.imageView);

		Button btn = (Button) this.findViewById(R.id.btnSnap);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takePicture();
			}
		});

		surfaceTexture = new SurfaceTexture(TEXTURE_OBJECT_NAME);
	}

	private void takePicture() {
		try {
			if (camera == null) {
				camera = Camera.open(0);
				camera.setPreviewTexture(surfaceTexture);

				Camera.Parameters params = camera.getParameters();
				// params.setPreviewSize(640, 480); // this is for preview
				params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				params.setPictureSize(640, 480); // this is for actual picture
													// taken!
				params.setPictureFormat(ImageFormat.JPEG);
				camera.setParameters(params);
			}

			camera.startPreview();
			camera.takePicture(null, null, new PictureCallback() {

				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					iv.setImageBitmap(bitmap);

					camera.stopPreview();
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
