package acm_graphics;

import android.graphics.Color;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * RandomGenerator
 * 
 * We can rebuild the ACM RandomGenerator class. If we use the Minimal Standard
 * Random Number Generator, we actually get really decent 32-bit random numbers.
 * 
 * @see https://en.wikipedia.org/wiki/Lehmer_random_number_generator
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RandomGenerator {
	private final long a = 48271; // also a good number
	private final long m = 2147483647L; // = 2^31 -1
	private long z = System.currentTimeMillis();

	public RandomGenerator() {
	}

	/**
	 * @return a random number between 0 and 2147483647 - 1
	 */
	public int nextInt() {
		z = (z * a) % m;
		return (int) z;
	}

	/**
	 * @return a random number between 0 <= r < n
	 */
	public int nextInt(int n) {
		if (n==0) {
			return 0;
		}
		return nextInt() % n;
	}
	
	/**
	 * Important: high should be larger than low!
	 * 
	 * @return a random number between low <= r < high
	 */
	public int nextInt(int low, int high) {
		return low + nextInt(high - low);
	}

	/**
	 * @return a random number between 0 <= r < 1
	 */
	public double nextDouble() {
		return nextInt() / (double) m;
	}

	/**
	 * Important: high should be larger than low!
	 * 
	 * @return a random number between low <= r < high
	 */
	public double nextDouble(double low, double high) {
		return low + nextDouble() * (high - low);
	}

	/**
	 * @return a random boolean with a 50/50 chance of true or false
	 */
	public boolean nextBoolean() {
		if (nextDouble() < 0.5) {
			return false;
		}
		return true;
	}
	
	public int nextColor() {
		return nextInt();
	}
	
	public int nextBrightColor() {
		return Color.rgb(nextInt(256), nextInt(256), nextInt(256));
	}
	

	/**
	 * If you do not want to depend on time, use a truly random number here.
	 * 
	 * @param s
	 *            should be less than m and larger than 1
	 */
	public void setSeed(int s) {
		if ((s > 0) && (s < m)) {
			z = s;
		}
	}
}
