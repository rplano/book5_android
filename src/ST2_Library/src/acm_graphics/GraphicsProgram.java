package acm_graphics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The touch/mouse is not totally correct (see ConnectTheClicks). <br/>
 * 
 * @author ralph
 */
public abstract class GraphicsProgram extends Activity implements Runnable {

	protected boolean waitingForTouch = true;
	private GView gv;
	private List<GObject> gobjects = Collections.synchronizedList(new ArrayList<GObject>());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(getLocalClassName(), "onCreate()");
		super.onCreate(savedInstanceState);

		gv = new GView(this, gobjects);
		setContentView(gv);

		init();
		new Thread(this).start();
	}

	public void init() {
		// may be overridden
		Toast.makeText(this, "Touch to start...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void run() {
		// does nothing, needs to be overridden
		while (true) {
			pause(50);
		}
	}
	
	protected int getWidth() {
		return gv.getWidth();
	}

	protected int getHeight() {
		return gv.getHeight();
	}

	protected void setSize(int w, int h) {
		// ignored;
	}

	protected void setBackground(int color) {
		gv.setBackground(color);
	}

	protected void add(GObject obj) {
		gobjects.add(obj);
	}

	protected void add(GObject obj, double x, double y) {
		obj.setLocation((int) x, (int) y);
		gobjects.add(obj);
	}

	protected void remove(GObject obj) {
		gobjects.remove(obj);
	}

	protected void removeAll() {
		gobjects.clear();
	}

	protected GObject getElementAt(int x, int y) {
		GObject retObj = null;
		for (GObject r : gobjects) {
			if (r.contains(x, y)) {
				retObj = r;
			}
		}
		return retObj;
	}

	protected GObject getElementAt(GObject obj) {
		GObject retObj = null;
		for (GObject r : gobjects) {
			if ((r.contains(obj.getX(), obj.getY())) && (r != obj)) {
				retObj = r;
			}
		}
		return retObj;
	}

	protected void waitForTouch() {
		//waitingForTouch = true;
		while (waitingForTouch) {
			pause(100);
		}
	}

	public void invalidateView() {
		gv.postInvalidate();		
	}

	protected void pause(int time) {
		invalidateView();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void addKeyListeners() {
		// ignored;
	}

	protected void addMouseListeners() {
		// ignored;
	}

	public void mouseMoved(int x, int y) {
		// should be overriden
		//Log.i("GraphicsProgram", "mouseMoved(" + x + "," + y + ")");
	}

	public void mousePressed(int x, int y) {
		// should be overriden
		//Log.i("GraphicsProgram", "mousePressed(" + x + "," + y + ")");
	}

	public void mouseReleased(int x, int y) {
		// should be overriden
		//Log.i("GraphicsProgram", "mouseReleased(" + x + "," + y + ")");
	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// Log.i(getLocalClassName(), "x=" + event.getX() + ", y=" +
//		// event.getY());
//		if (!waitingForTouch) { // needed to avoid trouble with mouseMoved()
//								// method being called to early
//			mouseMoved((int) event.getX(), (int) event.getY());
//			mousePressed((int) event.getX(), (int) event.getY());
//		}
//		waitingForTouch = false;
//		return super.onTouchEvent(event);
//	}

}
