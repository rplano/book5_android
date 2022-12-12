package variationenzumthema_pr8;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * DistanceActivity
 *
 * This activity uses the cameras getFocusDistances() method to estimate
 * distances.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DistanceActivity extends Activity {

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

					mCamera.autoFocus(new AutoFocusCallback() {

						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							float distances[] = new float[3];
							camera.getParameters().getFocusDistances(distances);
							String dists = "near=" + distances[0] + "m\n" + "optimal=" + distances[1] + "m\n" + "far="
									+ distances[2] + "m";
							Log.i("DistanceActivity", dists);
							Toast.makeText(getApplicationContext(), dists, Toast.LENGTH_LONG).show();
						}
					});

					Camera.Parameters params = mCamera.getParameters();
					params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
					mCamera.setParameters(params);

					mCamera.setPreviewDisplay(holder);
					mCamera.startPreview();
				} catch (Exception e) {
					Log.e("DistanceActivity", e.getMessage());
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