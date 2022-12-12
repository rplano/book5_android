package variationenzumthema_pr8;

import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Camera2PreviewActivity
 *
 * This activity demonstrates the use of the camera2 class.
 * 
 * @see For long time exposure
 *      https://stackoverflow.com/questions/32855925/camera2-api-how-to-set-long-exposure-times
 * 
 * @see https://android.jlelse.eu/the-least-you-can-do-with-camera2-api-2971c8c81b8b
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Camera2PreviewActivity extends Activity {
	private CameraManager cameraManager;
	// private int cameraFacing;
	private String cameraId;
	private TextureView.SurfaceTextureListener surfaceTextureListener;
	private Size previewSize;
	private HandlerThread backgroundThread;
	private Handler backgroundHandler;
	private CameraDevice cameraDevice;
	private TextureView textureView;

	// private CaptureRequest captureRequest;
	private CameraCaptureSession cameraCaptureSession;
	// private Builder captureRequestBuilder;
	private CameraDevice.StateCallback stateCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera2_preview_activity);

		textureView = (TextureView) this.findViewById(R.id.textureView);

		cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		// cameraFacing = CameraCharacteristics.LENS_FACING_BACK;

		surfaceTextureListener = new TextureView.SurfaceTextureListener() {
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
				setUpCamera();
				openCamera();
			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
				return false;
			}

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

			}
		};

		stateCallback = new CameraDevice.StateCallback() {
			@Override
			public void onOpened(CameraDevice cameraDevice) {
				Camera2PreviewActivity.this.cameraDevice = cameraDevice;
				createPreviewSession();
			}

			@Override
			public void onDisconnected(CameraDevice cameraDevice) {
				cameraDevice.close();
				Camera2PreviewActivity.this.cameraDevice = null;
			}

			@Override
			public void onError(CameraDevice cameraDevice, int error) {
				cameraDevice.close();
				Camera2PreviewActivity.this.cameraDevice = null;
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		openBackgroundThread();
		if (textureView.isAvailable()) {
			setUpCamera();
			openCamera();
		} else {
			textureView.setSurfaceTextureListener(surfaceTextureListener);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		closeCamera();
		closeBackgroundThread();
	}

	private void closeCamera() {
		if (cameraCaptureSession != null) {
			cameraCaptureSession.close();
			cameraCaptureSession = null;
		}

		if (cameraDevice != null) {
			cameraDevice.close();
			cameraDevice = null;
		}
	}

	private void closeBackgroundThread() {
		if (backgroundHandler != null) {
			backgroundThread.quitSafely();
			backgroundThread = null;
			backgroundHandler = null;
		}
	}

	private void setUpCamera() {
		try {
			for (String cameraId : cameraManager.getCameraIdList()) {
				CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
				if (cameraCharacteristics
						.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
					StreamConfigurationMap streamConfigurationMap = cameraCharacteristics
							.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
					previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
					this.cameraId = cameraId;
				}
			}
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void openCamera() {
		try {
			cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void openBackgroundThread() {
		backgroundThread = new HandlerThread("camera_background_thread");
		backgroundThread.start();
		backgroundHandler = new Handler(backgroundThread.getLooper());
	}

	private void createPreviewSession() {
		try {
			SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
			surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
			Surface previewSurface = new Surface(surfaceTexture);
			final Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			captureRequestBuilder.addTarget(previewSurface);

			cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
					new CameraCaptureSession.StateCallback() {

						@Override
						public void onConfigured(CameraCaptureSession cameraCaptureSession) {
							if (cameraDevice == null) {
								return;
							}

							try {
								CaptureRequest captureRequest = captureRequestBuilder.build();
								Camera2PreviewActivity.this.cameraCaptureSession = cameraCaptureSession;
								Camera2PreviewActivity.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
										null, backgroundHandler);
							} catch (CameraAccessException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

						}
					}, backgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}
}
