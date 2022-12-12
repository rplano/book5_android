package variationenzumthema_pr7;

import java.io.Serializable;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Point
 *
 * A simple wrapper for two integers that happens to be serializable.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Point implements Serializable {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}