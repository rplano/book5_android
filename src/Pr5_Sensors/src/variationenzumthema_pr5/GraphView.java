package variationenzumthema_pr5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GraphView
 *
 * A simple view to display sensor and other data graphically. It knows three
 * styles: HISTOGRAM, POINT, LINE. It displays the data like an oscilloscope,
 * new data coming in from the right, old data being shifted to the left.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphView extends View {

	public enum GraphStyle {
		HISTOGRAM, POINT, LINE
	}

	private final int GRID_COLOR = 0x11000000;

	private long startTime;

	private int mCanvasWidth;
	private int mCanvasHeight;

	private double min = 0;
	private double max = Float.MAX_VALUE;
	private double[] data;
	private int dataPointer = 0;

	private Paint backgroundForTextPaint;
	private Paint textPaint;
	private Paint gridPaint;
	private Paint borderPaint;
	private GraphStyle graphStyle = GraphStyle.HISTOGRAM;

	public GraphView(Context context) {
		super(context);

		backgroundForTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		backgroundForTextPaint.setColor(Color.WHITE);

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.BLACK);
		textPaint.setStrokeWidth(1);
		textPaint.setTextSize(48f);

		gridPaint = new Paint();
		gridPaint.setAntiAlias(true);
		gridPaint.setDither(true);
		gridPaint.setColor(GRID_COLOR);
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setStrokeWidth(1);

		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setDither(true);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(2);

		startTime = System.currentTimeMillis();
	}

	public void addDataPoint(double dat) {
		//Log.i("GraphView", "mCanvasWidth=" + mCanvasWidth);
		if (data != null && mCanvasWidth > 0) {
			data[dataPointer] = dat;
			dataPointer++;
			dataPointer = dataPointer % mCanvasWidth;
		}
	}

	public void setStyle(GraphStyle style) {
		this.graphStyle = style;
	}

	public void setColor(int color) {
		textPaint.setColor(color);
	}

	public void setStrokeWidth(int width) {
		textPaint.setStrokeWidth(width);
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setMax(double max) {
		this.max = max;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCanvasWidth = w;
		mCanvasHeight = h;
		data = new double[mCanvasWidth];
		dataPointer = 0;
	}

	public void onDraw(Canvas canvas) {

		drawGrid(canvas);
		drawBorder(canvas);

		// draw data
		switch (graphStyle) {
		case HISTOGRAM:
			float zero = (float) (1.0 - ((0 - min) / (max - min))) * mCanvasHeight;
			for (int i = 0; i < mCanvasWidth; i++) {
				int idx = (i + dataPointer) % mCanvasWidth;
				float value = (float) (1.0 - ((data[idx] - min) / (max - min))) * mCanvasHeight;
				canvas.drawLine(i, zero, i, value, textPaint);
			}
			break;

		case POINT:
			for (int i = 0; i < mCanvasWidth; i++) {
				int idx = (i + dataPointer) % mCanvasWidth;
				float value = (float) (1.0 - ((data[idx] - min) / (max - min))) * mCanvasHeight;
				canvas.drawPoint(i, value, textPaint);
			}
			break;

		case LINE:
			int idx = (0 + dataPointer) % mCanvasWidth;
			float value0 = (float) (1.0 - ((data[idx] - min) / (max - min))) * mCanvasHeight;
			for (int i = 1; i < mCanvasWidth; i++) {
				idx = (i + dataPointer) % mCanvasWidth;
				float value = (float) (1.0 - ((data[idx] - min) / (max - min))) * mCanvasHeight;
				canvas.drawLine(i - 1, value0, i, value, textPaint);
				value0 = value;
			}
			break;

		default:
			Log.i("onDraw", "unknown graphStyle");
			break;
		}

		// display frame rate
		long endTime = System.currentTimeMillis();
		long fps = 1000 / (endTime - startTime);
		startTime = endTime;
		canvas.drawRect(2, 2, 200, 70, backgroundForTextPaint);
		canvas.drawText("fps:" + fps, 20, 50, textPaint);
	}

	private void drawBorder(Canvas canvas) {
		canvas.drawRect(1, 1, getWidth() - 2, getHeight() - 1, borderPaint);
	}

	private void drawGrid(Canvas canvas) {
		for (int i = 0; i < getWidth(); i += 50) {
			canvas.drawLine(i, 0, i, getHeight(), gridPaint);
		}
		for (int i = 0; i < getHeight(); i += 50) {
			canvas.drawLine(0, i, getWidth(), i, gridPaint);
		}
	}
}