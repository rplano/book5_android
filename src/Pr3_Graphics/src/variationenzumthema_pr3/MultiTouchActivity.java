package variationenzumthema_pr3;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.Arrays;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr3.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MultiTouchActivity
 *
 * This activity demonstrates pinch zoom and rotate gestures. Use single finger
 * to drag, pinch with two finger to zoom, and rotate with three fingers. one
 * finger to move, two to zoom, and three to rotate the image.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MultiTouchActivity extends Activity {

	private MultiTouchImageView imageView;
	private FileChooser fc;
	private String currentPath = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createUI();
	}

	private void createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		imageView = new MultiTouchImageView(this);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ll.addView(imageView);
		setContentView(ll);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("currentPath", fc.getCurrentPath());
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		currentPath = sharedPref.getString("currentPath", "/sdcard/");

		fc = new FileChooser(this, currentPath);
		fc.setFileListener(new FileChooser.FileSelectedListener() {
			@Override
			public void fileSelected(final File file) {
				currentPath = file.getAbsolutePath();
				imageView.loadImageFromFile(currentPath);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Open").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// .setIcon(R.drawable.menu_add)
		menu.add("Save").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getTitle().toString()) {
		case "Open":
			fc.showDialog();
			return true;

		case "Save":
			imageView.saveImageToFile(currentPath + "_");
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * https://judepereira.com/blog/multi-touch-in-android-translate-scale-and-rotate/
	 * https://github.com/judepereira/android-multitouch/blob/master/src/com/multitouch/example/MultiTouch.java
	 * 
	 * @author Jude Pereira
	 */
	private class MultiTouchImageView extends ImageView implements View.OnTouchListener {

		// we can be in one of these 3 states
		private static final int NONE = 0;
		private static final int DRAG = 1;
		private static final int ZOOM = 2;
		private int mode = NONE;

		// these matrices will be used to move and zoom image
		private Matrix matrix = new Matrix();
		private Matrix savedMatrix = new Matrix();

		// remember some things for zooming
		private PointF start = new PointF();
		private PointF mid = new PointF();
		private float oldDist = 1f;
		private float d = 0f;
		private float newRot = 0f;
		private float[] lastEvent = null;

		public MultiTouchImageView(Context context) {
			super(context);
			this.setBackgroundColor(Color.BLACK);
			this.setScaleType(ImageView.ScaleType.MATRIX);
			this.setOnTouchListener(this);
			this.setImageResource(R.drawable.om);
		}

		public void saveImageToFile(String absolutePath) {
			try {
				FileOutputStream fos = new FileOutputStream(absolutePath);

				this.setDrawingCacheEnabled(true);
				this.buildDrawingCache();
				Bitmap bitmap = this.getDrawingCache();
				bitmap.compress(CompressFormat.PNG, 100, fos);
				this.setDrawingCacheEnabled(false);

				fos.flush();
				fos.close();
				Toast.makeText(this.getContext(), "Image was saved!", Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				Log.e("MultiTouchImageView.saveImage()", "" + e.getMessage());
				Toast.makeText(this.getContext(), "An error occured!", Toast.LENGTH_LONG).show();
			}
		}

		public void loadImageFromFile(String absolutePath) {
			Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
			if (bitmap != null) {
				init();
				this.setImageBitmap(bitmap);
			}
		}

		private void init() {
			mode = NONE;

			matrix = new Matrix();
			savedMatrix = new Matrix();

			start = new PointF();
			mid = new PointF();
			oldDist = 1f;
			d = 0f;
			newRot = 0f;
			lastEvent = null;

			setImageMatrix(matrix);
		}

		public boolean onTouch(View v, MotionEvent event) {
			ImageView view = (ImageView) v;
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = DRAG;
				lastEvent = null;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				lastEvent = new float[4];
				lastEvent[0] = event.getX(0);
				lastEvent[1] = event.getX(1);
				lastEvent[2] = event.getY(0);
				lastEvent[3] = event.getY(1);
				d = rotation(event);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				lastEvent = null;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					float dx = event.getX() - start.x;
					float dy = event.getY() - start.y;
					matrix.postTranslate(dx, dy);
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = (newDist / oldDist);
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
					if (lastEvent != null && event.getPointerCount() == 3) {
						newRot = rotation(event);
						float r = newRot - d;
						float[] values = new float[9];
						matrix.getValues(values);
						float tx = values[2];
						float ty = values[5];
						float sx = values[0];
						float xc = (view.getWidth() / 2) * sx;
						float yc = (view.getHeight() / 2) * sx;
						matrix.postRotate(r, tx + xc, ty + yc);
					}
				}
				break;
			}

			view.setImageMatrix(matrix);
			return true;
		}

		/**
		 * Determine the space between the first two fingers
		 */
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		/**
		 * Calculate the mid point of the first two fingers
		 */
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

		/**
		 * Calculate the degree to be rotated by.
		 *
		 * @param event
		 * @return Degrees
		 */
		private float rotation(MotionEvent event) {
			double delta_x = (event.getX(0) - event.getX(1));
			double delta_y = (event.getY(0) - event.getY(1));
			double radians = Math.atan2(delta_y, delta_x);
			return (float) Math.toDegrees(radians);
		}
	}

}

/**
 * Public domain
 * 
 * @see http://www.ninthavenue.com.au/simple-android-file-chooser
 * 
 * @author Roger Keays
 */
class FileChooser {
	private static final String PARENT_DIR = "..";

	private final Activity parent;
	private ListView list;
	private Dialog dialog;
	private File currentPath;
	private File[] filesInCurrentDir;

	// filter on file extension
	private String extension = null;

	private FileSelectedListener fileListener;

	public FileChooser(Activity activity, String sPath) {
		this.parent = activity;
		dialog = new Dialog(activity);
		list = new ListView(activity);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
				String fileChosen = (String) list.getItemAtPosition(which);
				File chosenFile = getChosenFile(fileChosen);
				if (chosenFile.isDirectory()) {
					refresh(chosenFile);
				} else {
					if (fileListener != null) {
						fileListener.fileSelected(chosenFile);
					}
					dialog.dismiss();
				}
			}
		});
		dialog.setContentView(list);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		File path = Environment.getRootDirectory();
		if (sPath != null) {
			path = new File(sPath);
		}
		refresh(path); // getExternalStorageDirectory());
	}

	public void showDialog() {
		dialog.show();
	}

	public String getCurrentPath() {
		return currentPath.getAbsolutePath();
	}

	public File[] getFilesInCurrentDir() {
		return filesInCurrentDir;
	}

	public void setExtension(String extension) {
		this.extension = (extension == null) ? null : extension.toLowerCase();
	}

	// file selection event handling
	public interface FileSelectedListener {
		void fileSelected(File file);
	}

	public FileChooser setFileListener(FileSelectedListener fileListener) {
		this.fileListener = fileListener;
		return this;
	}

	/**
	 * Sort, filter and display the files for the given path.
	 */
	private void refresh(File path) {
		this.currentPath = path;
		if (path.exists()) {
			File[] dirs = path.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return (file.isDirectory() && file.canRead());
				}
			});
			File[] files = path.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (!file.isDirectory()) {
						if (!file.canRead()) {
							return false;
						} else if (extension == null) {
							return true;
						} else {
							return file.getName().toLowerCase().endsWith(extension);
						}
					} else {
						return false;
					}
				}
			});

			// convert to an array
			int i = 0;
			String[] fileList;
			if (path.getParentFile() == null) {
				fileList = new String[dirs.length + files.length];
			} else {
				fileList = new String[dirs.length + files.length + 1];
				fileList[i++] = PARENT_DIR;
			}
			filesInCurrentDir = new File[files.length];

			Arrays.sort(dirs);
			Arrays.sort(files);
			for (File dir : dirs) {
				fileList[i++] = dir.getName();
			}
			int j = 0;
			for (File file : files) {
				fileList[i++] = file.getName();
				filesInCurrentDir[j++] = file;
			}

			// refresh the user interface
			dialog.setTitle(currentPath.getPath());
			list.setAdapter(new ArrayAdapter(parent, android.R.layout.simple_list_item_1, fileList) {
				@Override
				public View getView(int pos, View view, ViewGroup parent) {
					view = super.getView(pos, view, parent);
					((TextView) view).setSingleLine(true);
					return view;
				}
			});
		}
	}

	/**
	 * Convert a relative filename into an actual File object.
	 */
	private File getChosenFile(String fileChosen) {
		if (fileChosen.equals(PARENT_DIR)) {
			return currentPath.getParentFile();
		} else {
			return new File(currentPath, fileChosen);
		}
	}
}