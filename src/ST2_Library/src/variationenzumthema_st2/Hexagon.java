package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Graphics: Hexagon
 * 
 * Draw a hexagon using a polygon.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Hexagon extends GraphicsProgram {

	public void init() {
		setSize(300, 200);
		GPolygon hexa = new GPolygon();
		int size = 100;
		int pos = 400;
		hexa.addVertex(0, 0);
		hexa.addVertex(size, size * 2);
		hexa.addVertex(size * 3, size * 2);
		hexa.addVertex(size * 4, 0);
		hexa.addVertex(size * 3, -size * 2);
		hexa.addVertex(size, -size * 2);
		hexa.setFilled(true);
		hexa.setFillColor(Color.YELLOW);
		hexa.rotate(10);
		add(hexa, pos, pos + size * 2);
	}

}
