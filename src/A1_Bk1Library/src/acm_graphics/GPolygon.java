package acm_graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class GPolygon extends GObject implements GFillable {
	private int fillColor = Color.BLACK;
	private boolean filled = false;
	private Paint cPaint;

	private List<Point> vertices;

	public GPolygon() {
		this(0, 0);
	}

	public GPolygon(int x, int y) {
		super(x, y, 0, 0);
		cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cPaint.setStrokeWidth(2);
		vertices = new ArrayList<Point>();
	}

	public void addVertex(int x, int y) {
		vertices.add(new Point(x, y));
	}
	
	// rotate in degrees
	public void rotate(double theta) {
		for (int i = 0; i < vertices.size(); i++) {
			Point p = vertices.get(i);
			double alpha = -Math.toRadians(theta);
			double x1 = (p.x * Math.cos(alpha) - p.y * Math.sin(alpha));
			double y1 = (p.y * Math.cos(alpha) + p.x * Math.sin(alpha));
			p.x = x1;
			p.y = y1;
		}
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
//
//		drawPolygon(canvas);
//	}

    public void draw(Canvas canvas) {
        if (filled) {
            cPaint.setColor(this.fillColor);
            cPaint.setStyle(Paint.Style.FILL);
    		drawPolygon(canvas);
            cPaint.setColor(this.color);
            cPaint.setStyle(Paint.Style.STROKE);
    		drawPolygon(canvas);
        } else {
            cPaint.setColor(this.color);
            cPaint.setStyle(Paint.Style.STROKE);
    		drawPolygon(canvas);
        }
    }
	private void drawPolygon(Canvas canvas) {
		if (vertices.size() > 0) {
			Point p = vertices.get(0);

			Path path = new Path();
			path.reset();
			path.moveTo((int)p.x + x, (int)p.y + y);
			for (int i = 1; i < vertices.size(); i++) {
				p = vertices.get(i);
				path.lineTo((int)p.x + x, (int)p.y + y);
			}
			path.close();

			canvas.drawPath(path, cPaint);
		}
	}
	
	private class Point {
		public double x;
		public double y;
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

}
