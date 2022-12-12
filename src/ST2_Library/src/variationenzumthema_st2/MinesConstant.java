package variationenzumthema_st2;

import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Asteroids: MinesConstant
 * 
 * Constants that are needed by the MinesClone program.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public interface MinesConstant {

	/** Playing field should be 8x8 fields and 50 pixel per field */
	public static final int FIELD_SIZE = 8;

	/** Width and height of application window in pixels */
//	public static final int APPLICATION_WIDTH = FIELD_SIZE * PIXEL_PER_TILE;
//	public static final int APPLICATION_HEIGHT = FIELD_SIZE * PIXEL_PER_TILE;

	/** Number of mines in the game */
	public static final int NUMBER_OF_MINES = 10;

	/** Array of colors for the mine count labels */
	public static final int[] LABEL_COLORS = { Color.BLUE, Color.GREEN,
			Color.RED, Color.rgb(255,140,0), Color.MAGENTA, Color.CYAN, Color.rgb(255,192,203),
			Color.YELLOW };

}
