package variationenzumthema_ch6;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ProducerConsumerActivity
 *
 * This activity shows how to use the BlockingQueue to implement the classical
 * producer-consumer problem.
 * 
 * You should not use add() or remove() on BlockingQueue, because they do not
 * block!
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ProducerConsumerActivity extends Activity {

	private static final int DELAY = 1000;

	private Random rnd = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// can keep at most 10 elements:
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);

		Producer p = new Producer(queue);
		Consumer c = new Consumer(queue);
		Thread t1 = new Thread(p);
		Thread t2 = new Thread(c);
		t1.start();
		t2.start();
	}

	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	private class Producer implements Runnable {
		private BlockingQueue<String> queue;

		public Producer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				// send ten messages on average one per second
				for (int i = 0; i < 10; i++) {
					queue.put("Message #" + i);
					pause(DELAY / 2 + rnd.nextInt(DELAY / 2));
				}

				// end the consumer thread
				queue.put("quit");
				Log.i("Producer", "Producer is done.");

			} catch (InterruptedException e) {
				Log.i("Producer", e.getMessage());
			}

		}
	}

	private class Consumer implements Runnable {
		private BlockingQueue<String> queue;

		public Consumer(BlockingQueue<String> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				// receive messages
				String msg;
				while (!(msg = queue.take()).equals("quit")) {
					Log.i("Consumer", msg);
				}

				// we are done
				Log.i("Consumer", "Consumer is done.");

			} catch (InterruptedException e) {
				Log.i("Consumer", e.getMessage());
			}
		}
	}

}
