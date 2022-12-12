package acm_graphics;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * image to be loaded must be in the asset/ folder.
 * 
 * @author ralph
 */
public class GImage extends GObject {
	private Bitmap bitmap;

	public GImage(String imageFileName, Activity context) {
		super(0, 0);

		try {
			InputStream is = context.getAssets().open(imageFileName);
			bitmap = BitmapFactory.decodeStream(is);
			this.w = bitmap.getWidth();
			this.h = bitmap.getHeight();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GImage(Bitmap bitmap) {
		super(0, 0);
		this.bitmap = bitmap;
		this.w = bitmap.getWidth();
		this.h = bitmap.getHeight();
	}

	public GImage(int[][] array) {
		super(0, 0);
		
		// convert 2d array to 1d
		this.w = array.length;
		this.h = array[0].length;

		int[] bitMapArray = new int[w * h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				//array2d[i][j] = pixels[(j * w) + i];
				bitMapArray[(j * w) + i] = array[i][j];
			}
		}
		
		bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(bitMapArray, 0, w, 0, 0, w, h);
	}

	public void scale(double scale) {
		if (bitmap != null) {
			bitmap = bitmap.createScaledBitmap(bitmap, (int) (w * scale), (int) (h * scale), false);
			this.w = bitmap.getWidth();
			this.h = bitmap.getHeight();
		}
	}

	public void draw(Canvas canvas) {
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, x, y, null);
		}
	}

	public int[][] getPixelArray() {
		if (bitmap != null) {
			int[] pixels = new int[w * h];
			bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

			// convert 1d array to 2d
			int array2d[][] = new int[w][h];
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					array2d[i][j] = pixels[(j * w) + i];
				}
			}
			return array2d;
		}
		return null;
	}

	public static int getRed(int pixel) {
		return Color.red(pixel);
	}

	public static int getGreen(int pixel) {
		return Color.green(pixel);
	}

	public static int getBlue(int pixel) {
		return Color.blue(pixel);
	}

	public static int createRGBPixel(int red, int green, int blue) {
		return Color.rgb(red, green, blue);
	}
}
