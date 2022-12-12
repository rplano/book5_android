package variationenzumthema_pr1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * NumpadDemoActivity
 * 
 * Demonstrates how to use an Intent to send and receive data from another activity.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class NumpadDemoActivity extends Activity {
	private static final String TAG = "NumpadDemoActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int btnId = 42;

		Intent intnt = new Intent(getApplicationContext(), NumpadActivity.class);
		intnt.putExtra("id", btnId);
		startActivityForResult(intnt, 1);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				int id = data.getIntExtra("id", -1);
				int num = data.getIntExtra("num", -1);
				Log.i(TAG, "id=" + id + ", sNum=" + num);
			}
		}
	}
}
