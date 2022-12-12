package acm_graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public class GCompound extends GObject {
	
	private List<GObject> gobjects;

	public GCompound() {
		super(0, 0);
		gobjects = new ArrayList<GObject>();
	}

	protected void add(GObject obj) {
		gobjects.add(obj);
//		this.w = Math.max(this.w, obj.w);
//		this.h = Math.max(this.h, obj.h);
	}

	protected void add(GObject obj, double dx, double dy) {
		obj.setLocation((int) (x+dx), (int) (y+dy));
		gobjects.add(obj);
//		this.w = Math.max(this.w,(int) (obj.w+dx));
//		this.h = Math.max(this.h,(int) (obj.h+dy));
	}

	public void setLocation(int x1, int y1) {
		for (GObject r : gobjects) {
			int curX = r.getX();
			int curY = r.getY();
			int dx = curX - x;
			int dy = curY - y;
			r.x = x1+dx;
			r.y = y1+dy;
		}
		this.x = x1;
		this.y = y1;
	}
	
	public void draw(Canvas canvas) {
		for (GObject r : gobjects) {
			r.draw(canvas);
		}
	}
}
