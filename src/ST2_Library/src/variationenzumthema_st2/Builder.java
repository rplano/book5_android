package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: 6. Builder
 * 
 * Draw little blocks at the position the mouse was pressed.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Builder extends GraphicsProgram {
	// constants
	private final int BLOCK_SIZE = 100;

	public void init() { 
		//setSize(WIDTH, HEIGHT + 44);
		addMouseListeners();
	}

	public void mousePressed(int x, int y) {
//		int x = e.getX();
//		int y = e.getY();
		x = x / BLOCK_SIZE * BLOCK_SIZE;
		y = y / BLOCK_SIZE * BLOCK_SIZE;
		GRect block = new GRect(x, y,BLOCK_SIZE, BLOCK_SIZE);
		block.setColor(Color.RED);
		block.setFilled(true);
		block.setFillColor(Color.YELLOW);
		add(block);
	}
}
