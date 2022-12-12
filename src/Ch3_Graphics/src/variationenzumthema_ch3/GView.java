package variationenzumthema_ch3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

public class GView extends View {
	private final int SIZE = 40;
	private final int DELAY = 40;
	private Random rgen = new Random();
	private List<GOval> confettis = new ArrayList<GOval>();

	public GView(Context context) {
		super(context);
	}

	public void addNewConfetti() {
		// create randomly sized oval
		int width = SIZE / 2 + rgen.nextInt(SIZE);
		int x = -SIZE + rgen.nextInt(getWidth());
		int y = -SIZE + rgen.nextInt(getHeight());
		GOval oval = new GOval(x, y, width, width);

		// assign random color
		oval.setColor(rgen.nextInt());

		// add to list
		confettis.add(oval);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("onDraw","onDraw");
		setBackgroundColor(Color.WHITE);

		addNewConfetti();

		// draw all objects
		for (GOval ov : confettis) {
			ov.draw(canvas);
		}

		//pause(DELAY);
		//invalidate();
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}