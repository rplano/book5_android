package variationenzumthema_pr6;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * HeatMapActivity
 *
 * This activity is a demonstration of the heat equation.
 * 
 * @see https://en.wikipedia.org/wiki/Heat_equation
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HeatMapActivity extends Activity implements Runnable {
	private static final String TAG = "HeatMapActivity";

	public static final int DELAY = 50;
	public static final int NR_ROWS = 30;
	public static final int NR_COLUMNS = 30;
	public static final int DATA_MAX_VALUE = 100;
	public static final float TIME_DISSIPATION_FACTOR = 0.9f;
	public static final float SPACE_DISSIPATION_FACTOR = 0.1f;

	private HeatMapView gv;
	private float[][] data = new float[NR_ROWS][NR_COLUMNS];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		gv = new HeatMapView(this, data);
		setContentView(gv);

		new Thread(this).start();
	}

	@Override
	public void run() {
		setup();
		while (true) {
			applyHeatDissipation();
			pause(DELAY);
		}
	}

	private void applyHeatDissipation() {
		float[][] data2 = new float[NR_ROWS][NR_COLUMNS];

		for (int i = 1; i < NR_COLUMNS - 1; i++) {
			for (int j = 1; j < NR_ROWS - 1; j++) {
				// first reduce value by dissipation
				// data[i][j] = data[i][j] * 0.9f;
				float data0 = data[i][j];
				float delta = 0;
				for (int m = -1; m < 2; m++) {
					for (int n = -1; n < 2; n++) {
						float d = data[i + m][j + n] - data0;
						if (d > 0) {
							delta += d;
						}
					}
				}
				data2[i][j] = delta * SPACE_DISSIPATION_FACTOR;
			}
		}

		for (int i = 1; i < NR_COLUMNS - 1; i++) {
			for (int j = 1; j < NR_ROWS - 1; j++) {
				// first reduce value by dissipation
				data[i][j] = data[i][j] * TIME_DISSIPATION_FACTOR;
				data[i][j] += data2[i][j];
				if (data[i][j] < 0) {
					data[i][j] = 0;
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float dwidth = this.getWindow().getDecorView().getWidth() / NR_COLUMNS + 1;
		float dheight = this.getWindow().getDecorView().getHeight() / NR_ROWS + 1;
		int i = (int) (event.getX() / dwidth);
		int j = (int) (event.getY() / dheight);
		// float pressure = event.getPressure();
		// Log.i(MainActivity.APP_NAME + ".HeatMapActivity", "onTouchEvent():
		// "+pressure);
		data[i][j] = 100;
		return super.onTouchEvent(event);
	}

	private void setup() {
	}

	protected void pause(int time) {
		gv.postInvalidate();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class HeatMapView extends View {
		private float[] hsv = { 240f, 1f, 1f };
		private float[][] data;
		private Paint cPaint;

		public HeatMapView(Context context, float[][] data) {
			super(context);
			this.data = data;

			cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			cPaint.setStyle(Paint.Style.FILL);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// Log.i( "HeatMapView.onDraw()", "onDraw()");
			// setBackgroundColor(Color.WHITE);

			float dwidth = getWidth() / NR_COLUMNS + 1;
			float dheight = getHeight() / NR_ROWS + 1;
			for (int i = 0; i < NR_COLUMNS; i++) {
				for (int j = 0; j < NR_ROWS; j++) {
					float x = i * dwidth;
					float y = j * dheight;
					hsv[0] = (DATA_MAX_VALUE - data[i][j]) * 240 / DATA_MAX_VALUE;
					int col = Color.HSVToColor(hsv);
					// int col = floatToColor(data[i][j], DATA_MAX_VALUE);
					cPaint.setColor(col);
					canvas.drawRect(x, y, x + dwidth, y + dheight, cPaint);
				}
			}
		}

		private int floatToColor(float x, float max) {
			Log.i("HeatMapView.onDraw()", "onDraw()" + x + "," + max);
			float oneSixth = max / 4;
			x = x * 0.99f; // we want values between 0 and max, but excluding
							// max

			// pick color
			int col = Color.WHITE;
			int choice = (int) (x / oneSixth);
			float r = x % oneSixth;
			switch (choice) {
			case 0:
				// blue
				col = Color.rgb(0, (int) (r * 255 / oneSixth), 255);
				break;
			case 1:
				col = Color.rgb(0, 255, (int) (255 - r * 255 / oneSixth));
				break;
			case 2:
				col = Color.rgb((int) (r * 255 / oneSixth), 255, 0);
				break;
			case 3:
				col = Color.rgb(255, (int) (255 - r * 255 / oneSixth), 0);
				break;
			}

			return col;
		}

	}
}
