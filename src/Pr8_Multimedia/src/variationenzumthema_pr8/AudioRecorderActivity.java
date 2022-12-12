package variationenzumthema_pr8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AudioRecorderActivity
 *
 * This activity shows how to record and replay short audio messages.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AudioRecorderActivity extends Activity {

	private final int SAMPLE_RATE = 44100; // 44100, 22050, 16000, 11025 and
											// 8000

	private ListView lv;
	private List<String> words;

	private boolean isRecording = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_recorder_activity);

		final Button btn = (Button) findViewById(R.id.btnRecord);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isRecording) {
					isRecording = false;
					btn.setText("Record");
				} else {
					isRecording = true;
					btn.setText("Stop recording");

					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
					String fileName = dateFormat.format(date) + ".pcm";
					Recorder recorder = new Recorder(fileName);
					new Thread(recorder).start();
				}
			}
		});

		words = getFilesWithExtension(".pcm");

		lv = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, words);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String fileName = (String) ((ArrayAdapter) lv.getAdapter()).getItem(position);
				playAudio(fileName);
			}
		});
	}

	private List<String> getFilesWithExtension(final String extension) {
		List<String> words = new ArrayList<String>();
		File f = Environment.getExternalStorageDirectory();
		File[] files = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(extension);
			}
		});
		for (int i = 0; i < files.length; i++) {
			words.add(0, files[i].getName());
		}
		return words;
	}

	private void playAudio(String fileName) {
		Log.i(getLocalClassName(), "Playing audio...");
		File f = new File(Environment.getExternalStorageDirectory(), fileName);

		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			DataInputStream dis = new DataInputStream(bis);

			int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT);
			AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

			audioTrack.flush();
			audioTrack.play();

			while (dis.available() > 0) {
				short[] buffer = new short[bufferSize];

				int i = 0;
				while (i < bufferSize && dis.available() > 0) {
					buffer[i] = dis.readShort();
					i++;
				}

				audioTrack.write(buffer, 0, bufferSize);
			}

			audioTrack.stop();
			audioTrack.release();

			dis.close();
			bis.close();

		} catch (Exception e) {
			Log.i(getLocalClassName(), e.getMessage());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	class Recorder implements Runnable {
		private String fileName;

		public Recorder(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public void run() {

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

				while (isRecording) {
					int length = audioRecord.read(buffer, 0, bufferSize);
					for (int j = 0; j < length; j++) {
						dos.writeShort(buffer[j]);
					}
				}

				audioRecord.stop();
				audioRecord.release();
				Log.i(getLocalClassName(), "finished recording.");

				dos.close();
				bos.close();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						words.add(0, fileName);
						((ArrayAdapter) lv.getAdapter()).notifyDataSetChanged();
					}
				});

			} catch (Exception e) {
				Log.i(getLocalClassName(), e.getMessage());
			}
		}
	}
}
