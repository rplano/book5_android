package variationenzumthema_ch3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * GConfettiActivity
 *
 * This activity shows how to implement the confetti example using a class
 * inspired by ACM's GOval.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class GraphicsProgramActivity extends Activity implements Runnable {
	private final int SIZE = 40;
	private final int DELAY = 40;

	private  View gv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gv = new GView(this);
		gv.setBackgroundColor(Color.WHITE);
		setContentView(gv);

		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);

		//Thread th = new Thread(this);
		//th.start();
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			pause(100);
			gv.postInvalidate();
		}
	}

	public void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

}
