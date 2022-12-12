package variationenzumthema_st4;

import java.util.ArrayList;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PrimitiveRoots
 *
 * Diffie-Hellman: This program finds the "Primitive roots modulo n" for numbers
 * n less than 17. For larger n, the data type long is not big enough, one needs
 * to use BigInteger.
 *
 * @see https://en.wikipedia.org/wiki/Primitive_root_modulo_n
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PrimitiveRoots {
	public static void main(String[] args) {

		List<Integer> cps = findCoPrimes(5);
		System.out.println(cps);

		// for (int n = 1; n <= 17; n++) {
		// // int n = 13;
		// List<Integer> primitiveroots = findPrimitiveRoots(n);
		// System.out.println("primitive roots of " + n + " are: "
		// + primitiveroots);
		// }
	}

	private static List<Integer> findPrimitiveRoots(int n) {
		List<Integer> primitiveroots = new ArrayList<Integer>();
		List<Integer> coprimes = findCoPrimes(n);
		for (int g = 1; g < n; g++) {
			boolean isPrimitiveRoot = true;
			for (Integer coprime : coprimes) {
				// System.out.print(coprime + ": ");
				boolean isCongruent = false;
				for (int k = 1; k <= n; k++) {
					long x = power(g, k) % n;
					if (x < 0) {
						System.err.println("sign overflow: long is to small as a data type!");
					}
					// System.out.print(x + ",");
					if (coprime == x) {
						isCongruent = true;
						break;
					}
				}
				if (!isCongruent) {
					isPrimitiveRoot = false;
				}
				// System.out.println();
			}
			// System.out.println(g + " isPrimitiveRoot of " + n + ": "
			// + isPrimitiveRoot);
			if (isPrimitiveRoot) {
				primitiveroots.add(g);
			}
		}
		return primitiveroots;
	}

	private static List<Integer> findCoPrimes(int n) {
		List<Integer> coprimes = new ArrayList<Integer>();
		for (int a = 1; a <= n; a++) {
			if (gcd(a, n) == 1) {
				coprimes.add(a);
			}
		}
		return coprimes;
	}

	private static void findRoot(int g, int n) {
		for (int k = 0; k < n; k++) {
			long a = power(g, k);
			if (g == a % n) {
				System.out.println("primitive root: g=" + g + ", k=" + k + ", n=" + n);
			}
		}
	}

	public static long power(long x, long n) {
		long power = 1;
		for (int i = 0; i < n; i++) {
			power *= x;
		}
		return power;
	}

	public static int gcd(int a, int b) {
		while (b != 0) {
			if (a > b)
				a = a - b;
			else
				b = b - a;
		}
		return a;
	}

	private static boolean isPrime(int num) {
		if (num < 2)
			return false;
		if (num == 2)
			return true;
		if (num % 2 == 0)
			return false;
		for (int i = 3; i * i <= num; i += 2) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}
}
