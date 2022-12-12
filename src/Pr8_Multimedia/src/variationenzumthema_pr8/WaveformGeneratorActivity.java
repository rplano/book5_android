package variationenzumthema_pr8;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WaveformGeneratorActivity
 *
 * This activity generates different wave forms and plays them as audio.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WaveformGeneratorActivity extends Activity {

	private final double FREQUENCY = 220;
	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private int playTimeInSecs = 1; // play for one second
	private int waveForm = 0; // 0 = sin, 1 = square, 2 = sawtooth, 3 =
								// triangle, 4 = staircase

	private AudioTrack audioTrack;
	private short[] data;
	private GraphView2 gv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gv = new GraphView2(this);
		gv.setMin(Short.MIN_VALUE);
		gv.setMax(Short.MAX_VALUE);
		gv.setStyle(GraphView2.GraphStyle.LINE);
		gv.setColor(Color.RED);
		gv.setStrokeWidth(2);
		setContentView(gv);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		int width = getResources().getDisplayMetrics().widthPixels;
		waveForm = 0;
		if (x > 4 * width / 5) {
			waveForm = 4;
		} else if (x > 3 * width / 5) {
			waveForm = 3;
		} else if (x > 2 * width / 5) {
			waveForm = 2;
		} else if (x > 1 * width / 5) {
			waveForm = 1;
		}
		generateSound(SAMPLE_RATE * playTimeInSecs);

		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 2 * data.length, AudioTrack.MODE_STATIC);
		audioTrack.flush();
		audioTrack.write(data, 0, data.length);
		audioTrack.play();

		return true;
	}

	@Override
	protected void onPause() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
		}
		super.onPause();
	}

	private void generateSound(int bufferSize) {
		data = new short[bufferSize];
		double factor = 2 * Math.PI / (SAMPLE_RATE / FREQUENCY);
		for (int i = 0; i < data.length; i++) {
			data[i] = (short) (Short.MAX_VALUE * 0.95 * function(factor, i));
			gv.addDataPoint(data[i]);
		}
		gv.postInvalidate();
	}

	private double function(double factor, int i) {
		switch (waveForm) {
		case 1:
			return functionSquare(factor, i);
		case 2:
			return functionSawTooth(factor, i);
		case 3:
			return functionTriangle(factor, i);
		case 4:
			return functionStaircase(factor, i);
		default:
			return functionSin(factor, i);
		}
	}

	/**
	 * Use any periodic function with a period of 2*PI
	 * 
	 * @return values between -1 and +1
	 */
	private double functionSin(double factor, int i) {
		return Math.sin(i * factor);
	}

	private double functionSquare(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI);
		if (x < Math.PI) {
			return -1;
		} else {
			return +1;
		}
	}

	private double functionSawTooth(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI);
		return x / (Math.PI) - 1;
	}

	private double functionTriangle(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI);
		if (x < Math.PI) {
			return 2 * x / (Math.PI) - 1;
		} else {
			return 2 * (2 * Math.PI - x) / (Math.PI) - 1;
		}
	}

	private double functionStaircase(double factor, int i) {
		double x = i * factor;
		x = x % (2 * Math.PI); // 0..2*PI
		x = x / (2 * Math.PI); // 0..1
		x = x * 10; // 0..9.999
		int s = (int) x; // 0..9
		s = s - 5; // -5..4
		return s / 5.0;
	}

}
