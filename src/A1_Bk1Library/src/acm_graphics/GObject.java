package acm_graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public abstract class GObject {
	protected int x, y, w, h;
	protected int color = Color.BLACK;

	public GObject(int x, int y, int w, int h) {
		this.setBounds(x, y, w, h);
	}

	public GObject(int w, int h) {
		this(0, 0, w, h);
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setBounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		if (w < 0) {
			w = -w;
		}
		this.w = w;
		if (h < 0) {
			h = -h;
		}
		this.h = h;
	}

	public void scale(double sf) {
		this.scale(sf, sf);
	}

	public void scale(double sw, double sh) {
		this.w = (int) (sw * w);
		this.h = (int) (sh * h);
	}

	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public boolean contains(int x1, int y1) {
		Rect r = new Rect(x, y, x + w, y + h);
		return r.contains(x1, y1);
	}

	public void sendToBack() {
		// does nothing
	}

	abstract void draw(Canvas canvas);
}
