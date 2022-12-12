package variationenzumthema_pr8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AugmentedRealityActivity
 *
 * This activity displays some text over-layed over a preview of the camera.
 *
 * @see https://code.tutsplus.com/tutorials/android-sdk-augmented-reality-camera-sensor-setup--mobile-7873
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AugmentedRealityActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FrameLayout fl = new FrameLayout(this);
		fl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(fl);

		CameraView cameraView = new CameraView(this);
		fl.addView(cameraView);

		OverlayView overlayView = new OverlayView(this);
		fl.addView(overlayView);
	}

	private class CameraView extends SurfaceView implements SurfaceHolder.Callback {
		private Camera mCamera;

		public CameraView(Context context) {
			super(context);

			SurfaceHolder holder = this.getHolder();
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			holder.addCallback(this);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera = Camera.open();
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception e) {
				Log.e("CameraView", e.getMessage());
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// do nothing
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

	}

	private class OverlayView extends View {

		public OverlayView(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.GREEN);
			paint.setTextSize(128f);
			canvas.drawText("Hello World!", getWidth() / 2 - 300, 200, paint);
		}
	}
}