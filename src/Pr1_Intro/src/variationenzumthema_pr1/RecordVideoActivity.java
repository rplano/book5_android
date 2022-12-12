package variationenzumthema_pr1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * RecordVideoActivity
 * 
 * Demonstrates how to use an Intent to record a video.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RecordVideoActivity extends Activity {

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn = new Button(this);
		btn.setText("Start video recording");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(intent, 1);
			}
		});
		ll.addView(btn);

		tv = new TextView(this);
		ll.addView(tv);

		setContentView(ll);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Uri videoLocation = intent.getData();
				Toast.makeText(this, "Video:" + videoLocation, Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Recording was canceled", Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}
}