package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Tree
 * 
 * Draws a nice looking random tree.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Tree extends GraphicsProgram {
	
	private RandomGenerator rgen = new RandomGenerator();

	public void run() {
		//setSize(WIDTH, HEIGHT + 48);
		waitForTouch();
		int size = getHeight()/4;
		drawBranch(getWidth() / 2, getHeight(), Math.toRadians(90), size);
	}

	public void mousePressed(int x, int y) {
		removeAll();
		int size = getHeight()/4;
		drawBranch(getWidth() / 2, getHeight(), Math.toRadians(90), size);
	}

	private void drawBranch(double x0, double y0, double angle,
			double length) {
		double x1 = x0 - Math.cos(angle) * length;
		double y1 = y0 - Math.sin(angle) * length;
		drawLine(x0, y0, x1, y1, length);

		// base case
		if (length < 10)
			return;

		// recursive case
		double bendAngle = Math.toRadians(rgen.nextDouble(-10, 10));
		double branchAngle = Math.toRadians(rgen.nextDouble(-30, 30));
		drawBranch(x1, y1, angle + bendAngle - branchAngle,
				length * rgen.nextDouble(0.6, 0.8));
		drawBranch(x1, y1, angle + bendAngle + branchAngle,
				length * rgen.nextDouble(0.6, 0.8));
	}

	private void drawLine(double x1, double y1, double x2, double y2, double len) {
		int thick = (int) (len / 10);
		int col = Color.rgb(101, 67, 33); // Color.BLACK;
		if (len < 10) {
			col = Color.GREEN;
			thick = 2;
		}
		GThickLine line = new GThickLine((int)x1, (int)y1, (int)x2, (int)y2, thick);
		line.setColor(col);
		add(line);
	}
}