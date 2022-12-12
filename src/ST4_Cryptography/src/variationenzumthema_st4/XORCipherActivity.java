package variationenzumthema_st4;

import acm_graphics.GImage;
import acm_graphics.GraphicsProgram;
import acm_graphics.RandomGenerator;
import android.content.res.Configuration;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * XORCipherActivity
 *
 * A simple demonstration of the XOR cipher using an image.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class XORCipherActivity extends GraphicsProgram {
	private RandomGenerator rgen = new RandomGenerator();

	public void init() {
		// encryption
		// GImage image = new GImage("Taj_Mahal_(Edited).jpeg", this);
		GImage image = new GImage("Mona_Lisa.jpg", this);
		// image.scale(0.5);
		add(image, 0, 0);

		int seed = 42;
		GImage randomImage = createRandomImage(400, 400, seed);
		add(randomImage, 400, 0);

		GImage xorImage = xorTwoImages(image, randomImage);
		add(xorImage, 800, 0);

		// decryption
		GImage xorImageCopy = new GImage(xorImage.getPixelArray());
		add(xorImageCopy, 0, 400);

		GImage randomImage2 = createRandomImage(400, 400, seed);
		add(randomImage2, 400, 400);

		GImage xorImage2 = xorTwoImages(xorImageCopy, randomImage2);
		add(xorImage2, 800, 400);

		invalidateView();
	}

	private GImage xorTwoImages(GImage image1, GImage image2) {
		int[][] array1 = image1.getPixelArray();
		int[][] array2 = image2.getPixelArray();

		int height = array1.length;
		int width = array1[0].length;
		int[][] array3 = new int[width][height];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				array3[i][j] = array1[i][j] ^ array2[i][j];
			}
		}
		return new GImage(array3);
	}

	private GImage createRandomImage(int width, int height, int seed) {
		rgen.setSeed(seed);

		int[][] array = new int[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				array[i][j] = rgen.nextInt();
			}
		}
		return new GImage(array);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
}