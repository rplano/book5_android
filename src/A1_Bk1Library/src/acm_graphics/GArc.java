package acm_graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class GArc extends GObject implements GFillable {
	private int fillColor = Color.BLACK;
	private boolean filled = false;
	private Paint cPaint;
	private int startAngle;
	private int sweepAngle;

	public GArc(int w, int h, int startAngle,int sweepAngle) {
		super(w, h);
		this.startAngle = startAngle;
		this.sweepAngle = sweepAngle;
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStrokeWidth(2);
	}

	public void setStartAngle(int angle) {
		this.startAngle = angle;
	}

	public void setSweepAngle(int angle) {
		this.sweepAngle = angle;
	}

	public void setFilled(boolean b) {
		this.filled = b;
	}

	public void setFillColor(int col) {
		this.fillColor = col;
		//this.color = col;
	}

//	public void draw(Canvas canvas) {
//		cPaint.setColor(this.color);
//		if (filled) {
//			cPaint.setStyle(Paint.Style.FILL);
//		} else {
//			cPaint.setStyle(Paint.Style.STROKE);
//		}
//		RectF mRectF = new RectF(this.x, this.y, this.x + this.w, this.y + this.h);
//		canvas.drawArc(mRectF, startAngle, sweepAngle, true, cPaint);
//	}
    public void draw(Canvas canvas) {
        if (filled) {
            cPaint.setColor(this.fillColor);
            cPaint.setStyle(Paint.Style.FILL);
    		RectF mRectF = new RectF(this.x, this.y, this.x + this.w, this.y + this.h);
    		canvas.drawArc(mRectF, startAngle, sweepAngle, true, cPaint);
            cPaint.setColor(this.color);
            cPaint.setStyle(Paint.Style.STROKE);
    		//RectF mRectF = new RectF(this.x, this.y, this.x + this.w, this.y + this.h);
    		canvas.drawArc(mRectF, startAngle, sweepAngle, true, cPaint);
        } else {
            cPaint.setColor(this.color);
            cPaint.setStyle(Paint.Style.STROKE);
    		RectF mRectF = new RectF(this.x, this.y, this.x + this.w, this.y + this.h);
    		canvas.drawArc(mRectF, startAngle, sweepAngle, true, cPaint);
        }
    }

}
