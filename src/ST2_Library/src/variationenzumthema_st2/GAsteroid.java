package variationenzumthema_st2;

import acm_graphics.*;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Asteroids: Asteroids
 * 
 * Asteroids is an arcade space shooter game. The player controls a spaceship in
 * an asteroid field. The object of the game is to shoot and destroy asteroids
 * while not colliding with them.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */ 
public class GAsteroid extends GRect implements AsteroidConstants {

	public int vx = 0;
	public int vy = 0;

	public GAsteroid(double x, double y) {
		super((int)x, (int)y, ASTEROID_SIZE, ASTEROID_SIZE);
	}

	public void move() {
		this.move(vx, vy);
	}
}