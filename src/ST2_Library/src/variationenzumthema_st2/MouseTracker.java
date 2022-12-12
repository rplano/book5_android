package variationenzumthema_st2;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: 7. MouseTracker
 * 
 * Draws a label with the mouse position at the position where the mouse is.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MouseTracker extends GraphicsProgram {

	// instance variables
	private GLabel lbl;

	public void init() { 
		//setSize(WIDTH, HEIGHT);

		lbl = new GLabel("", 0, 0);
		lbl.setFont("Arial-bold-54");
		add(lbl);

		addMouseListeners();
	}

	public void mouseMoved(int x, int y) {
//		int x = e.getX();
//		int y = e.getY();
		lbl.setLabel("x=" + x + ",y=" + y);
		lbl.setLocation(x, y);
	}
}
