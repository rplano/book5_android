package acm_graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public class GThickLine extends GObject {
	private Paint cPaint;

	public GThickLine(int x, int y, int w, int h, int thickness) {
		super(x, y, w, h);
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStrokeWidth(thickness);
	}

	public void draw(Canvas canvas) {
		cPaint.setColor(this.color);
		canvas.drawLine(this.x, this.y, this.w, this.h, cPaint);
	}

}