package variationenzumthema_pr8;

import java.util.ArrayList;
import java.util.LinkedList;

import acm_graphics.GImage;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SpectrogramActivity
 *
 * This activity shows a live spectrogram of the microphone.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SpectrogramActivity extends Activity implements Runnable {

	private final int SAMPLE_RATE = 8000; // 44100, 22050, 16000, 11025 and 8000

	private final int NR_OF_LINES = 512;
	private final int BUFFER_SIZE = NR_OF_LINES * 2;
	private final int DELAY = 50;

	private int bufferSize = 0;
	private short[] data;
	private ArrayList<double[]> spectrum = new ArrayList<double[]>();

	private SpectrumView sv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
		// AudioFormat.CHANNEL_IN_MONO,
		// AudioFormat.ENCODING_PCM_16BIT);
		bufferSize = BUFFER_SIZE;
		data = new short[bufferSize];

		this.sv = new SpectrumView(this, spectrum);
		setContentView(sv);

		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			recordAudio();
			doFFT();
			sv.postInvalidate();
			pause(DELAY);
		}
	}

	private void recordAudio() {
		try {
			AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
					AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

			audioRecord.startRecording();
			audioRecord.read(data, 0, bufferSize);
			audioRecord.stop();

		} catch (Exception e) {
			Log.i(getLocalClassName(), e.getMessage());
		}
	}

	private double min;
	private double max;

	// Length something like 8000/x, where x is the frequency in Hz
	// that is your cutoff: 100 Hz is a good choice as suggested by
	// http://en.wikipedia.org/wiki/Audiogram
	public final int M = 160; // = 0.02*fs
	public final int SIZE_Y = BUFFER_SIZE / M;

	private ArrayList<double[]> doFFT() {
		spectrum.clear();

		int N = nextpow2(4 * M);// zero padding for interpolation
		FFT fft = new FFT(N);

		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		double[] audioData = shortArrayToDoubleArray(data);
		for (int j = 0; j < (data.length - M); j += M) {
			double[] re = new double[N];
			double[] im = new double[N];

			System.arraycopy(audioData, j, re, 0, M);
			fft.fft(re, im);

			double[] magn = new double[N / 2];
			for (int k = 0; k < magn.length; k++) {
				magn[k] = Math.sqrt(re[k] * re[k] + im[k] * im[k]);
				if (magn[k] < min) {
					min = magn[k];
				}
				if (magn[k] > max) {
					max = magn[k];
				}
			}
			spectrum.add(magn);
		}
		return spectrum;
	}

	private double[] shortArrayToDoubleArray(short[] source) {
		double[] dest = new double[source.length];
		for (int i = 0; i < source.length; i++) {
			dest[i] = source[i];
		}
		return dest;
	}

	protected void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static int nextpow2(int i) {
		int pow2 = 2;
		while (pow2 <= i) {
			pow2 *= 2;
		}
		return pow2;
	}

	class SpectrumView extends View {
		private final int SPREAD = 16;
		private final int NR_OF_SPECTRA_IMAGES = 10;

		private FixedSizeQueue<GImage> imgSpectra;
		private ArrayList<double[]> spectrum;
		private int viewWidth = -1;
		private int viewHeight = -1;

		public SpectrumView(Context context, ArrayList<double[]> spectrum) {
			super(context);
			this.spectrum = spectrum;
			this.imgSpectra = new FixedSizeQueue<GImage>(NR_OF_SPECTRA_IMAGES);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			if (h > 0) {
				this.viewWidth = w;
				this.viewHeight = h;
			}
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			setBackgroundColor(Color.BLACK);
			if (spectrum != null && spectrum.size() > 0) {
				GImage img = drawSpectrum(spectrum);
				// img.draw(canvas);
				imgSpectra.add(img);
				int xPos = 0;
				for (GImage spec : imgSpectra) {
					spec.setX(xPos);
					spec.draw(canvas);
					xPos += spec.getWidth();
				}
			}
		}

		private GImage drawSpectrum(ArrayList<double[]> spectrum) {
			int[][] imgArray = new int[(SIZE_Y) * SPREAD][viewWidth];

			int w = spectrum.size();
			int h = spectrum.get(0).length;

			double scaleX = (double) SIZE_Y / (double) w;
			double scaleY = (double) viewWidth / (double) h;
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int sX = (int) (i * scaleX);
					int sY = (int) ((h - j - 1) * scaleY);

					double col = spectrum.get(i)[j];

					float[] hsv = new float[3];
					hsv[0] = (float) (240.0 * col / max);
					hsv[1] = 1;
					hsv[2] = 1;
					int colRGB = Color.HSVToColor(hsv);

					for (int k = 0; k < SPREAD; k++) {
						imgArray[sX * SPREAD + k][sY] = colRGB;
					}
				}
			}
			GImage img = new GImage(imgArray);
			return img;
		}
	}

	/**
	 * @see https://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java/14322473
	 */
	public class FixedSizeQueue<E> extends LinkedList<E> {
		private int limit;

		public FixedSizeQueue(int limit) {
			this.limit = limit;
		}

		@Override
		public boolean add(E o) {
			boolean added = super.add(o);
			while (added && size() > limit) {
				super.remove();
			}
			return added;
		}
	}
}
