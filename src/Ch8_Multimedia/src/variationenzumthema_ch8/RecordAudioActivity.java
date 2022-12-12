package variationenzumthema_ch8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import variationenzumthema.ch8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RecordAudioActivity
 *
 * This activity shows how to record a audio using the AudioRecord class.
 *
 * @see https://stackoverflow.com/questions/17192256/recording-wav-with-android-audiorecorder
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RecordAudioActivity extends Activity {

	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_audio_activity);

		final Button btnRecord = (Button) findViewById(R.id.btnRecord);
		btnRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				record("AudioRecording.pcm");
				convertToWave("AudioRecording");
			}
		});

		final Button btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play("AudioRecording.wav");
			}
		});
	}

	private void play(String fileName) {
		File f = new File(Environment.getExternalStorageDirectory(), fileName);
		try {
			MediaPlayer player = new MediaPlayer();
			player.setDataSource(f.getAbsolutePath());
			player.prepare();
			player.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void record(String fileName) {
		File f = new File(Environment.getExternalStorageDirectory(), fileName);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
			DataOutputStream dos = new DataOutputStream(bos);

			int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			short[] buffer = new short[bufferSize];

			AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
					AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

			audioRecord.startRecording();

			// record for roughly 2 seconds:
			for (int i = 0; i < 20; i++) {
				int length = audioRecord.read(buffer, 0, bufferSize);
				for (int j = 0; j < length; j++) {
					dos.writeShort(buffer[j]);
				}
			}

			audioRecord.stop();
			audioRecord.release();
			Log.i(getLocalClassName(), "Finished recording.");
			Toast.makeText(this, "Finished recording.\n" + f.getAbsolutePath(), Toast.LENGTH_SHORT).show();

			dos.close();
			bos.close();

		} catch (Exception e) {
			Log.i(getLocalClassName(), e.getMessage());
		}
	}

	private void convertToWave(String fileName) {
		// first read pcm from file into a byte array
		byte[] buffer = null;
		try {
			File f = new File(Environment.getExternalStorageDirectory(), fileName + ".pcm");
			buffer = new byte[(int) f.length()];
			BufferedInputStream bos = new BufferedInputStream(new FileInputStream(f));
			DataInputStream dos = new DataInputStream(bos);
			dos.read(buffer);
			dos.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// then convert byte array to wav
		File f = new File(Environment.getExternalStorageDirectory(), fileName + ".wav");
		WaveConverter converter = new WaveConverter(SAMPLE_RATE, (short) 1, buffer, 0, buffer.length - 1);
		converter.writeToFile(f);
	}

	/**
	 * @see https://stackoverflow.com/questions/17192256/recording-wav-with-android-audiorecorder
	 * @author user3660664
	 */
	private class WaveConverter {
		private final int LONGINT = 4;
		private final int VERYSMALLINT = 1;
		private final int SMALLINT = 2;
		private final int INTEGER = 4;
		private final int ID_STRING_SIZE = 4;
		private final int WAV_RIFF_SIZE = LONGINT + ID_STRING_SIZE;
		private final int WAV_FMT_SIZE = (4 * SMALLINT) + (INTEGER * 2) + LONGINT + ID_STRING_SIZE;
		private final int WAV_DATA_SIZE = ID_STRING_SIZE + LONGINT;
		private final int WAV_HDR_SIZE = WAV_RIFF_SIZE + ID_STRING_SIZE + WAV_FMT_SIZE + WAV_DATA_SIZE;
		private final short PCM = 1;
		private final int SAMPLE_SIZE = 2;
		int cursor, nSamples;
		byte[] output;

		public WaveConverter(int sampleRate, short nChannels, short[] data, int start, int end) {
			nSamples = end - start + 1;
			cursor = 0;
			output = new byte[nSamples * SMALLINT + WAV_HDR_SIZE];
			buildHeader(sampleRate, nChannels);
			writeData(data, start, end);
		}

		public WaveConverter(int sampleRate, short nChannels, byte[] data, int start, int end) {
			nSamples = end - start + 1;
			cursor = 0;
			output = new byte[nSamples * VERYSMALLINT + WAV_HDR_SIZE];
			buildHeader(sampleRate, nChannels);
			writeData(data, start, end);
		}

		// ------------------------------------------------------------
		private void buildHeader(int sampleRate, short nChannels) {
			write("RIFF");
			write(output.length);
			write("WAVE");
			writeFormat(sampleRate, nChannels);
		}

		// ------------------------------------------------------------
		public void writeFormat(int sampleRate, short nChannels) {
			write("fmt ");
			write(WAV_FMT_SIZE - WAV_DATA_SIZE);
			write(PCM);
			write(nChannels);
			write(sampleRate);
			write(nChannels * sampleRate * SAMPLE_SIZE);
			write((short) (nChannels * SAMPLE_SIZE));
			write((short) 16);
		}

		// ------------------------------------------------------------
		public void writeData(short[] data, int start, int end) {
			write("data");
			write(nSamples * SMALLINT);
			for (int i = start; i <= end; write(data[i++]))
				;
		}

		// ------------------------------------------------------------
		public void writeData(byte[] data, int start, int end) {
			write("data");
			write(nSamples * VERYSMALLINT);
			for (int i = start; i <= end; i += 2) {
				write(data[i + 1]);
				write(data[i]);
			}
		}

		// ------------------------------------------------------------
		private void write(byte b) {
			output[cursor++] = b;
		}

		// ------------------------------------------------------------
		private void write(String id) {
			if (id.length() != ID_STRING_SIZE)
				Log.i("", "String " + id + " must have four characters.");
			else {
				for (int i = 0; i < ID_STRING_SIZE; ++i)
					write((byte) id.charAt(i));
			}
		}

		// ------------------------------------------------------------
		private void write(int i) {
			write((byte) (i & 0xFF));
			i >>= 8;
			write((byte) (i & 0xFF));
			i >>= 8;
			write((byte) (i & 0xFF));
			i >>= 8;
			write((byte) (i & 0xFF));
		}

		// ------------------------------------------------------------
		private void write(short i) {
			write((byte) (i & 0xFF));
			i >>= 8;
			write((byte) (i & 0xFF));
		}

		// ------------------------------------------------------------
		public boolean writeToFile(File path) {
			boolean ok = false;

			try {
				// File path = new File(getFilesDir(), filename);
				FileOutputStream outFile = new FileOutputStream(path);
				outFile.write(output);
				outFile.close();
				ok = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				ok = false;
			} catch (IOException e) {
				ok = false;
				e.printStackTrace();
			}
			return ok;
		}
	}
}