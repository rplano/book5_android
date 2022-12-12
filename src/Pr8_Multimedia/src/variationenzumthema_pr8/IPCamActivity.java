package variationenzumthema_pr8;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * IPCamActivity
 *
 * This activity demonstrates how to use your smartphone as an IP camera.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class IPCamActivity extends Activity implements SurfaceHolder.Callback, Runnable {
	private final String encoding = "ASCII";
	private final int DELAY = 300; // delay between two images in ms
	private final int PORT = 4242;

	private boolean safeToTakePicture = false; // @see
												// https://stackoverflow.com/questions/21723557/java-lang-runtimeexception-takepicture-failed

	private boolean isRunning = true;
	private Camera camera;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ip_cam_activity);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		TextView tv = (TextView) findViewById(R.id.textView);
		tv.setText(Util.getMyLocalIpAddress().getHostAddress() + ":" + PORT);

		SurfaceView surface = (SurfaceView) this.findViewById(R.id.surfaceView);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.setFixedSize(400, 300);

		Thread th = new Thread(this);
		th.start();
	}

	private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (result == null) {
				result = size;
			} else {
				int resultArea = result.width * result.height;
				int newArea = size.width * size.height;

				if (newArea < resultArea) {
					result = size;
				}
			}
		}

		return (result);
	}

	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i("IPCamActivity.surfaceChanged", "width="+width+", height="+height);
		Camera.Parameters params = camera.getParameters();
		//Camera.Size size = getBestPreviewSize(width, height, params);
		//Camera.Size pictureSize = getSmallestPictureSize(params);
		//if (size != null && pictureSize != null) {

			params.setPreviewSize(640, 480); // this is for preview
			params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			params.setPictureSize(640, 480); // this is for actual picture
			// taken!
			params.setPictureFormat(ImageFormat.JPEG);
			camera.setParameters(params);

			camera.startPreview();
			safeToTakePicture = true;
		//}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open();
			camera.setPreviewDisplay(holder);
		} catch (Throwable t) {
			Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
			// Toast.makeText(CameraLauncherActivity.this, t.getMessage(),
			// Toast.LENGTH_LONG).show();
		}
		// try {
		// camera = Camera.open();
		// camera.setPreviewDisplay(holder);
		//
		// Camera.Parameters params = camera.getParameters();
		// params.setPreviewSize(640, 480); // this is for preview
		// params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		// params.setPictureSize(640, 480); // this is for actual picture
		// // taken!
		// params.setPictureFormat(ImageFormat.JPEG);
		// camera.setParameters(params);
		//
		// camera.startPreview();
		// safeToTakePicture = true;
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
	}

	public void run() {
		try {
			ServerSocket server = new ServerSocket(PORT);
			while (isRunning) {
				Socket socket = server.accept();
				Log.i("IPCamActivity", socket.getInetAddress().getHostAddress());

				final OutputStream out = socket.getOutputStream();

				// send first part of header
				String httpHeader1 = "HTTP/1.0 200 OK\r\n"
						+ "Content-type: multipart/x-mixed-replace; boundary=--BoundaryString\r\n\r\n";
				out.write(httpHeader1.getBytes(encoding));

				// send image stream
				while (isRunning && socket.isConnected()) {
					if (safeToTakePicture) {
						safeToTakePicture = false;
						camera.takePicture(null, null, new PictureCallback() {

							@Override
							public void onPictureTaken(byte[] data, Camera camera) {
								Log.i("IPCamActivity", "Image taken. Size=" + data.length);

						        camera.startPreview();
						        
								try {
									// send second part of header
									String httpHeader2 = "--BoundaryString\r\n" + "Content-type: image/jpeg\r\n"
											+ "Content-length: " + data.length + "\r\n\r\n";
									out.write(httpHeader2.getBytes(encoding));
									out.write(data);
									out.flush();

								} catch (IOException e) {
									e.printStackTrace();
								}
								safeToTakePicture = true;
							}
						});
					}
					pause(DELAY);
				}

				out.close();
				socket.close();
				socket = null;
			}
			server.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
