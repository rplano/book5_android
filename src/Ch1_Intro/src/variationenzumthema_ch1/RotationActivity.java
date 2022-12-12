package variationenzumthema_ch1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RotationActivity
 *
 * Demonstrate how to handle change of orientation of device.
 * 
 * Also look at PaintActivity in Chapter 2 for another way to handle rotation of
 * device.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RotationActivity extends Activity {
	private static final String TAG = "RotationActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate() ");
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("Click me!");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick()");
				Intent i = new Intent(v.getContext(), MainActivity.class);
				startActivity(i);
			}
		});

		ll.addView(btn);
		setContentView(ll);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
		// setContentView(R.layout.myLayout);
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart()");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume()");
		super.onResume();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart()");
		super.onRestart();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
	}
}