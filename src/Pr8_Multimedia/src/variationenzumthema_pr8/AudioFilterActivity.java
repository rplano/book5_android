package variationenzumthema_pr8;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AudioFilterActivity
 *
 * This activity shows the effect of low-pass and high-pass filter on audio
 * files.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AudioFilterActivity extends Activity {
	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000

	private float LOW_PASS_FACTOR = 0.9f;

	private AudioTrack audioTrack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_filter_activity);

		Button btnNoFilter = (Button) findViewById(R.id.btnNoFilter);
		btnNoFilter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playMusic(0);
			}
		});

		Button btnHighPassFilter = (Button) findViewById(R.id.btnHighPassFilter);
		btnHighPassFilter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playMusic(1);
			}
		});

		Button btnLowPassFilter = (Button) findViewById(R.id.btnLowPassFilter);
		btnLowPassFilter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playMusic(2);
			}
		});
	}

	private void playMusic(int mode) {
		try {
			int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
			audioTrack.flush();
			audioTrack.play();

			InputStream is = getResources().openRawResource(R.raw.trap_beat2);
			BufferedInputStream bis = new BufferedInputStream(is);
			DataInputStream dis = new DataInputStream(bis);

			readWavHeader(dis);

			while (dis.available() > 0) {
				short[] data = new short[bufferSize];
				int i = 0;
				while (dis.available() > 0 && i < bufferSize) {
					data[i] = readShort(dis);
					i++;
				}

				short[] filtered = null;
				switch (mode) {
				case 1:
					LOW_PASS_FACTOR = 0.3f;
					filtered = filterLowFrequencies(data);
					break;
				case 2:
					LOW_PASS_FACTOR = 0.9f;
					filtered = filterHighFrequencies(data);
					break;
				default:
					filtered = data;
					break;
				}

				audioTrack.write(filtered, 0, filtered.length);
			}

			dis.close();

			audioTrack.stop();
			audioTrack.release();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private short[] filterLowFrequencies(short[] recordingData) {
		short[] highPassArray = new short[recordingData.length];
		short avg = 0;
		for (int i = 0; i < recordingData.length; i++) {
			avg = lowPass(recordingData[i], avg);
			highPassArray[i] = highPass(recordingData[i], avg);
		}
		return highPassArray;
	}

	private short[] filterHighFrequencies(short[] recordingData) {
		short[] lowPassArray = new short[recordingData.length];
		short avg = 0;
		for (int i = 0; i < recordingData.length; i++) {
			avg = lowPass(recordingData[i], avg);
			lowPassArray[i] = avg;
		}
		return lowPassArray;
	}

	private short lowPass(short current, short average) {
		return (short) (average * LOW_PASS_FACTOR + current * (1 - LOW_PASS_FACTOR));
	}

	private short highPass(short current, short average) {
		return (short) (current - average);
	}

	/**
	 * dumps the first 44 bytes containing the wave header.
	 */
	private void readWavHeader(DataInputStream dis) throws IOException {
		for (int i = 0; i < 11; i++) {
			dis.readInt();
		}
	}

	/**
	 * Windows is little endian.
	 */
	private short readShort(InputStream in) throws IOException {
		return (short) (in.read() | (in.read() << 8));
	}
}
