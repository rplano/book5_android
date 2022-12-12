package variationenzumthema_ch3;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class  GOval {
	private int x, y, w, h;
	private Paint paint;

	public GOval(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.paint.setStyle(Paint.Style.FILL);
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void draw(Canvas canvas) {
		canvas.drawOval(new RectF(this.x, this.y, this.x + this.w, this.y + this.h), paint);
	}
}