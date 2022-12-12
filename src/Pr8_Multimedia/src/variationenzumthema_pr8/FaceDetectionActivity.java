package variationenzumthema_pr8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
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
 * FaceDetectionActivity
 *
 * This activity shows how to detect faces with Android's FaceDetectionListener.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FaceDetectionActivity extends Activity {

	private FaceDetectionListener faceDetectionListener;
	private Face[] detectedFaces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FrameLayout fl = new FrameLayout(this);
		fl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(fl);

		CameraView cameraView = new CameraView(this);
		fl.addView(cameraView);

		final OverlayView overlayView = new OverlayView(this);
		fl.addView(overlayView);

		faceDetectionListener = new FaceDetectionListener() {

			@Override
			public void onFaceDetection(Face[] faces, Camera camera) {
				detectedFaces = faces;
				overlayView.invalidate();
			}
		};
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
				mCamera = Camera.open(1);
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();

				mCamera.setFaceDetectionListener(faceDetectionListener);
				mCamera.startFaceDetection();

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

			if (detectedFaces != null) {
				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(8);
				paint.setColor(Color.GREEN);

				float scaleX = (float) (getWidth() / 2000.0);
				float scaleY = (float) (getHeight() / 2000.0);

				for (int i = 0; i < detectedFaces.length; i++) {
					// Log.i("OverlayView", ""+detectedFaces[i].rect);
					Rect rect = detectedFaces[i].rect;

					float left = (1000 - rect.left) * scaleX;
					float top = (1000 + rect.top) * scaleY;
					float right = (1000 - rect.right) * scaleX;
					float bottom = (1000 + rect.bottom) * scaleY;

					canvas.drawRect(left, top, right, bottom, paint);
				}

			}
		}
	}
}