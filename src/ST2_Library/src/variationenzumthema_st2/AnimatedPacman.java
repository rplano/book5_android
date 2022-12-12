package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: AnimatedPacman
 * 
 * Draws an animated Pacman.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AnimatedPacman extends GraphicsProgram {
	private final int DELAY = 50;

	public void run() {
		setSize(300, 200);

		GArc pacman = new GArc(150, 150, 0, 360);
		//GOval pacman = new GOval(150, 150);
		pacman.setFilled(true);
		pacman.setFillColor(Color.YELLOW);
		add(pacman, 275, 202);
		
		GOval eye = new GOval(15, 15);
		eye.setFilled(true);
		add(eye, 345, 232);

		int angle = 0;
		int step = 5;
		while (true) {
			pacman.setStartAngle(angle);
			pacman.setSweepAngle(360 - 2 * angle);
			angle += step;
			if (angle > 40 || angle <= 1) {
				step = -step;
			}
			pause(DELAY);
		}

	}
}
