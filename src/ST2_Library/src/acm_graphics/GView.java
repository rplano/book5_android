package acm_graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class GView extends View {

	private List<GObject> gobjects;
	private int backgroundColor = Color.WHITE;

	// neede for frame rate
	private Paint textPaint;
	private long startTime;

	public GView(Context context, List<GObject> gobjects) {
		super(context);
		this.gobjects = gobjects;

		// needed for frame rate
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStrokeWidth(1);
		textPaint.setTextSize(48f);
	}

	public void setBackground(int color) {
		this.backgroundColor = color;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Log.i( "onDraw()", "onDraw()");
		setBackgroundColor(backgroundColor);
		synchronized (gobjects) {
			for (GObject r : gobjects) {
				r.draw(canvas);
			}
		}

		// display frame rate
		long endTime = System.currentTimeMillis();
		long fps = 1000;
		if (endTime != startTime) {
			fps = 1000 / (endTime - startTime);
		}
		startTime = endTime;
		textPaint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, 200, 70, textPaint);
		textPaint.setColor(Color.BLACK);
		canvas.drawText("fps:" + fps, 20, 50, textPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.i("GView", "onTouchEvent");
		GraphicsProgram program = (GraphicsProgram) this.getContext();
		// needed to avoid trouble with mouseMoved() method is called to early
		if (!program.waitingForTouch) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				program.mousePressed((int) event.getX(), (int) event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				program.mouseMoved((int) event.getX(), (int) event.getY());
				break;
			case MotionEvent.ACTION_UP:
				program.mouseReleased((int) event.getX(), (int) event.getY());
				break;
			}
		}
		program.waitingForTouch = false;
		invalidate();
		return true;

	}
}
