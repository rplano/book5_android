package variationenzumthema_ch6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PhilosopherProgram
 *
 * This standard Java program demonstrates dead lock and race condition using the well known dining philosopher example.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PhilosopherProgram {
	// depending on your computers speed you may have to adjust the DELAY time:
	private static final int DELAY = 100;

	private List<ChopStick> chopSticks;

	public PhilosopherProgram(char whichOne) {
		// create list and add two chop sticks:
		chopSticks = Collections.synchronizedList(new ArrayList<ChopStick>());
		chopSticks.add(new ChopStick("A"));
		chopSticks.add(new ChopStick("B"));

		// create two philosophers:
		Philosopher p1 = new Philosopher("1", chopSticks, whichOne);
		Philosopher p2 = new Philosopher("2", chopSticks, whichOne);
		Thread t1 = new Thread(p1);
		Thread t2 = new Thread(p2);
		t1.start();
		t2.start();
	}

	public static void main(String[] args) {
		if (args.length == 1) {
			PhilosopherProgram pp = new PhilosopherProgram(args[0].charAt(0));
		} else {
			System.out.println("Usage: java PhilosopherProgram [1-4]");
		}
	}

	private class ChopStick {
		private String name;

		public ChopStick(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private class Philosopher implements Runnable {
		private String name;
		private List<ChopStick> chopSticks;
		private int liveForce = 5;
		private char whichOne;

		public Philosopher(String name, List<ChopStick> chopSticks,
				char whichOne) {
			this.name = name;
			this.chopSticks = chopSticks;
			this.whichOne = whichOne;
		}

		@Override
		public void run() {
			switch (whichOne) {
			case '1':
				run1();
				break;
			case '2':
				run2();
				break;
			case '3':
				run3();
				break;
			case '4':
				run4();
				break;
			default:
				System.out.println("we should never get here....");
				break;
			}
		}

		/**
		 * Here we use wait() and notify(). Now everything is fine and they
		 * lived happily ever after. Once in a while you see one philosopher
		 * only having 3 lives.
		 */
		public void run4() {
			ChopStick cs1 = null;
			ChopStick cs2 = null;
			while (liveForce > 0) {
				liveForce--;
				System.out.println("Philosopher #" + name + " still has "
						+ liveForce + " lives.");

				// wait between 0 and DELAY milliseconds
				pause(new Random().nextInt(DELAY));

				// try to get both chop sticks
				synchronized (chopSticks) {
					if (chopSticks.size() < 2) {
						try {
							chopSticks.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						cs1 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs1.getName());
						cs2 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs2.getName());
					}
				}

				// do we have both chop sticks?
				if ((cs1 != null) && (cs2 != null)) {
					liveForce = 5;
					// now return the chop sticks:
					synchronized (chopSticks) {
						chopSticks.add(cs1);
						chopSticks.add(cs2);
						cs1 = null;
						cs2 = null;
						System.out.println("Returned chop sticks.");
						chopSticks.notifyAll();
					}
				}
			}
			System.out.println("Philosopher #" + name + " is dead.");
		}

		/**
		 * Although we use synchronized and we return the chop sticks, still
		 * after a while we get sometimes dead lock and sometimes race
		 * condition! Although it takes longer now.
		 */
		private void run3() {
			ChopStick cs1 = null;
			ChopStick cs2 = null;
			while (liveForce > 0) {
				liveForce--;
				System.out.println("Philosopher #" + name + " still has "
						+ liveForce + " lives.");

				// wait between 0 and DELAY milliseconds
				pause(new Random().nextInt(DELAY));

				// try to get both chop sticks
				// lock the other guy(s) out
				synchronized (chopSticks) {
					if (chopSticks.size() > 0) {
						cs1 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs1.getName());
					}

					if (chopSticks.size() > 0) {
						cs2 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs2.getName());
					}
				}

				// do we have both chop sticks?
				if ((cs1 != null) && (cs2 != null)) {
					liveForce = 5;
					// return chop sticks
					chopSticks.add(cs1);
					chopSticks.add(cs2);
					cs1 = null;
					cs2 = null;
					System.out.println("Returned chop sticks.");
				}
			}
			System.out.println("Philosopher #" + name + " is dead.");
		}

		/**
		 * Nothing synchronized: but we do return the chop sticks. And when we
		 * can't get the second chop stick, we return the first one.
		 */
		private void run2a() {
			ChopStick cs1 = null;
			ChopStick cs2 = null;
			while (liveForce > 0) {
				liveForce--;
				System.out.println("Philosopher #" + name + " still has "
						+ liveForce + " lives.");

				// wait between 0 and DELAY milliseconds
				pause(new Random().nextInt(DELAY));

				// try to get both chop sticks
				if (cs1 == null) {
					if (chopSticks.size() > 0) {
						cs1 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs1.getName());
					}
				} else {
					if (chopSticks.size() > 0) {
						cs2 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs2.getName());
					} else {
						chopSticks.add(cs1);
						System.out.println("Philosopher #" + name
								+ " returned chop stick " + cs1.getName());
						cs1 = null;
					}
				}

				// do we have both chop sticks?
				if ((cs1 != null) && (cs2 != null)) {
					liveForce = 5;
					// return chop sticks
					chopSticks.add(cs1);
					chopSticks.add(cs2);
					cs1 = null;
					cs2 = null;
					System.out.println("Returned chop sticks.");
				}
			}
			System.out.println("Philosopher #" + name + " is dead.");
		}

		/**
		 * Nothing synchronized: but we do return the chop sticks.
		 */
		private void run2() {
			ChopStick cs1 = null;
			ChopStick cs2 = null;
			while (liveForce > 0) {
				liveForce--;
				System.out.println("Philosopher #" + name + " still has "
						+ liveForce + " lives.");

				// wait between 0 and DELAY milliseconds
				pause(new Random().nextInt(DELAY));

				// try to get both chop sticks
				if (cs1 == null) {
					if (chopSticks.size() > 0) {
						cs1 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs1.getName());
					}
				} else {
					if (chopSticks.size() > 0) {
						cs2 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs2.getName());
					}
				}

				// do we have both chop sticks?
				if ((cs1 != null) && (cs2 != null)) {
					liveForce = 5;
					// return chop sticks
					chopSticks.add(cs1);
					chopSticks.add(cs2);
					cs1 = null;
					cs2 = null;
					System.out.println("Returned chop sticks.");
				}
			}
			System.out.println("Philosopher #" + name + " is dead.");
		}

		/**
		 * Nothing synchronized: sometimes both starve to death (dead lock),
		 * sometimes only one (race condition).
		 */
		private void run1() {
			ChopStick cs1 = null;
			ChopStick cs2 = null;
			while (liveForce > 0) {
				liveForce--;
				System.out.println("Philosopher #" + name + " still has "
						+ liveForce + " lives.");

				// wait between 0 and DELAY milliseconds
				pause(new Random().nextInt(DELAY));

				// try to get both chop sticks
				if (cs1 == null) {
					if (chopSticks.size() > 0) {
						cs1 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs1.getName());
					}
				} else {
					if (chopSticks.size() > 0) {
						cs2 = chopSticks.remove(0);
						System.out.println("Philosopher #" + name
								+ " got chop stick " + cs2.getName());
					}
				}

				// do we have both chop sticks?
				if ((cs1 != null) && (cs2 != null)) {
					liveForce = 5;
				}
			}
			System.out.println("Philosopher #" + name + " is dead.");
		}

		private void pause(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
			}
		}
	}

}
