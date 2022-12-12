package variationenzumthema_ch8;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import variationenzumthema.ch8.R;

/**
 * @author ralph
 *
 */
/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * CameraPreviewActivity
 *
 * This activity shows how to show the camera preview.
 * 
 * @see https://developer.android.com/training/camera/cameradirect
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CameraPreviewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview_activity);

		SurfaceView surface = (SurfaceView) this.findViewById(R.id.surfaceView);

		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			private Camera mCamera;

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				// do nothing
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					mCamera = Camera.open();
					mCamera.setPreviewDisplay(holder);
					mCamera.startPreview();
				} catch (Exception e) {
					Log.e("CameraPreviewActivity", e.getMessage());
				}
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		});
	}
}