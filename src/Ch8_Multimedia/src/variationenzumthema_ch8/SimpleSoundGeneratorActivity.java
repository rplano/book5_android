package variationenzumthema_ch8;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SimpleSoundGeneratorActivity
 *
 * This activity shows how to generate sound with the AudioTrack class.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SimpleSoundGeneratorActivity extends Activity {

	private final double FREQUENCY = 440; // A4 is the A above middle C
	private final int SAMPLE_RATE = 16000; // 44100, 22050, 16000, 11025 and
											// 8000
	private AudioTrack audioTrack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int playTimeInSecs = 1; // play for one second
		short[] data = generateSound(SAMPLE_RATE * playTimeInSecs);

		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 2 * data.length, AudioTrack.MODE_STATIC);
		audioTrack.flush();
		audioTrack.write(data, 0, data.length);
		audioTrack.play();
	}

	@Override
	protected void onPause() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
			audioTrack = null;
		}
		super.onPause();
	}

	private short[] generateSound(int bufferSize) {
		short[] data = new short[bufferSize];
		double factor = 2 * Math.PI / (SAMPLE_RATE / FREQUENCY);
		for (int i = 0; i < data.length; i++) {
			data[i] = (short) (Short.MAX_VALUE * Math.sin(factor * i));
		}
		return data;
	}
}
