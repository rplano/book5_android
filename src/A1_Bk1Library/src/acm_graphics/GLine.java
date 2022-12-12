package acm_graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public class GLine extends GObject {
	private Paint cPaint;

    public GLine(int x, int y, int w, int h) {
        super(x,y,w,h);
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStrokeWidth(2);
    }

    public void draw(Canvas canvas) {
        cPaint.setColor(this.color);
        canvas.drawLine(this.x, this.y, this.w, this.h, cPaint);
    }
    
}
