package variationenzumthema_pr8;

import java.util.concurrent.BlockingQueue;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Player
 *
 * A thread that continually plays audio that comes in through a BlockingQueue.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Player implements Runnable {
	private int SAMPLE_RATE;
	private int bufferSizeInByte = 0;
	public boolean isPlaying = false;
	private final BlockingQueue<short[]> queue;

	public Player(BlockingQueue<short[]> queue, int SAMPLE_RATE, int bufferSizeInByte) {
		this.queue = queue;
		this.SAMPLE_RATE = SAMPLE_RATE;
		this.bufferSizeInByte = bufferSizeInByte;
	}

	public void run() {
		try {
			isPlaying = true;
			AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSizeInByte, AudioTrack.MODE_STREAM);
			audioTrack.flush();
			audioTrack.play();

			while (isPlaying) {
				short[] data = queue.take();
				audioTrack.write(data, 0, data.length);
			}

			audioTrack.stop();
			audioTrack.release();

		} catch (InterruptedException e) {
			Log.i("Player", e.getMessage());
		}
	}
}