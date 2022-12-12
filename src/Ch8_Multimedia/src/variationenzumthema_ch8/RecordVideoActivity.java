package variationenzumthema_ch8;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import variationenzumthema.ch8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RecordVideoActivity
 *
 * This activity demonstrates how to record a video using the camera.
 *
 * @see http://developer.android.com/guide/topics/media/camera.html#capture-video
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RecordVideoActivity extends Activity implements SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private Camera mCamera;
	private MediaRecorder mMediaRecorder;

	private boolean isRecording = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_video_activity);

		final Button btn = (Button) this.findViewById(R.id.btnRecord);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isRecording) {
					stopRecordingAndReleaseCamera();
					btn.setText("Capture");
					isRecording = false;

				} else {
					if (prepareVideoRecorder("/sdcard/RecordVideoActivity.mp4")) {
						mMediaRecorder.start();
						btn.setText("Stop");
						isRecording = true;

					} else {
						Toast.makeText(getApplicationContext(), "Could not get access to camera and/or recorder.",
								Toast.LENGTH_LONG).show();
						releaseMediaRecorder();
					}
				}
			}
		});

		SurfaceView view = (SurfaceView) this.findViewById(R.id.surfaceView);
		holder = view.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder();
		releaseCamera();
	}

	/**
	 * http://developer.android.com/guide/topics/media/camera.html#capture-video
	 * 
	 * @return
	 */
	private boolean prepareVideoRecorder(String fileName) {
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

		// Step 4: Set output file
		// mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
		mMediaRecorder.setOutputFile(fileName);

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(holder.getSurface());

		// Step 6: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(getLocalClassName(), "IllegalStateException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d(getLocalClassName(), "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	private void stopRecordingAndReleaseCamera() {
		mMediaRecorder.stop();
		releaseMediaRecorder();
		mCamera.lock();

		try {
			mCamera.stopPreview();
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(getLocalClassName(), "stopRecordingAndReleaseCamera(): " + e);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(getLocalClassName(), "surfaceCreated(): " + e);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		try {
			mCamera.stopPreview();
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(getLocalClassName(), "surfaceChanged(): " + e);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		releaseCamera();
	}

	private void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}
}
