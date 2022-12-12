package variationenzumthema_st2;

import acm_graphics.*;
import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Agrar: TrafficLight
 * 
 * Draw an animated traffic light.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TrafficLight extends GraphicsProgram {
	// constants
	private final int SIZE = 60;
	private final int OFFSET = 20;
	private final int DELAY = 1000;
	private final int POSITION = (1000-(SIZE + OFFSET))/2 ; 

	// instance variables
	private GOval redLight;
	private GOval yellowLight;
	private GOval greenLight;

	private int currentLight = 0;

	public void run() {
		setup();

		// game loop
		while (true) {
			switchLight();
			pause(DELAY);
		}
	}

	private void switchLight() {
		if (currentLight == 0) {
			redLight.setFillColor(Color.RED);
			yellowLight.setFillColor(Color.BLACK);
			greenLight.setFillColor(Color.BLACK);
		} else if (currentLight == 1) {
			redLight.setFillColor(Color.RED);
			yellowLight.setFillColor(Color.YELLOW);
			greenLight.setFillColor(Color.BLACK);
		} else {
			redLight.setFillColor(Color.BLACK);
			yellowLight.setFillColor(Color.BLACK);
			greenLight.setFillColor(Color.GREEN);
		}
		currentLight++;
		currentLight = currentLight % 3;
	}

	private void setup() {
		//setSize(300, 200);

		GRect housing = new GRect(SIZE + OFFSET, (SIZE + OFFSET) * 3);
		housing.setFilled(true);
		add(housing, POSITION + 100, POSITION - 15);

		int x = POSITION + 100 + OFFSET / 2;
		int y = POSITION + OFFSET - 15;
		redLight = new GOval(SIZE, SIZE);
		drawLight(redLight, x, y, Color.RED);
		y += SIZE + OFFSET / 2;
		yellowLight = new GOval(SIZE, SIZE);
		drawLight(yellowLight, x, y, Color.YELLOW);
		y += SIZE + OFFSET / 2;
		greenLight = new GOval(SIZE, SIZE);
		drawLight(greenLight, x, y, Color.GREEN);
	}

	private void drawLight(GOval light, int x, int y, int col) {
		light.setFilled(true);
		light.setColor(col);
		add(light, x, y);
	}
}
