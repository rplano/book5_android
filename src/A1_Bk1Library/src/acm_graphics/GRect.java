package acm_graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class GRect extends GObject implements GFillable {
    private int fillColor = Color.BLACK;
    private boolean filled = false;
	private Paint cPaint;

    public GRect(int w, int h) {
		this(0, 0, w, h);
    }

    public GRect(int x, int y, int w, int h) {
        super(x,y,w,h);
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStrokeWidth(2);
    }

    public void setFilled(boolean b) {
        this.filled = b;
    }

    public void setFillColor(int col) {
        this.fillColor = col;
		//this.color = col;
    }

    public void draw(Canvas canvas) {
        if (filled) {
            cPaint.setColor(this.fillColor);
            cPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, cPaint);
            cPaint.setColor(this.color);
            cPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, cPaint);
        } else {
            cPaint.setColor(this.color);
            cPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, cPaint);
        }
    }
}

