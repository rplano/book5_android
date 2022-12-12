package variationenzumthema_pr8;

import java.util.concurrent.BlockingQueue;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Recorder
 *
 * A thread that continually records audio and sends it to a BlockingQueue.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Recorder implements Runnable {
	private int SAMPLE_RATE = 16000;
	private int bufferSizeInByte = 0;
	public boolean isRecording = false;
	private final BlockingQueue<short[]> queue;

	public Recorder(BlockingQueue<short[]> queue, int SAMPLE_RATE, int bufferSizeInByte) {
		this.queue = queue;
		this.SAMPLE_RATE = SAMPLE_RATE;
		this.bufferSizeInByte = bufferSizeInByte;
	}

	@Override
	public void run() {
		try {
			isRecording = true;
			short[] data = new short[bufferSizeInByte];
			AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
					AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInByte);

			audioRecord.startRecording();

			while (isRecording) {
				int length = audioRecord.read(data, 0, bufferSizeInByte);
				// make a copy and add to queue
				short[] copy = new short[bufferSizeInByte];
				System.arraycopy(data, 0, copy, 0, copy.length);
				queue.put(copy);
			}

			audioRecord.stop();
			audioRecord.release();

		} catch (Exception e) {
			Log.i("Recorder", e.getMessage());
		}

	}
}