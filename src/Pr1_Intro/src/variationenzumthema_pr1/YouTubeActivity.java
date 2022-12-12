package variationenzumthema_pr1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * YouTubeActivity
 *
 * Shows how to start YouTube, Google Maps and OpenStreetMap.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class YouTubeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		Button btn1 = new Button(this);
		btn1.setText("Start YouTube");
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("https://youtu.be/QOtapACvpcY");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		Button btn2 = new Button(this);
		btn2.setText("Start Google Maps");
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://maps.google.com/?q=Am%20Katharinenkloster%206,90403%20Nuremberg,Germany");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		Button btn3 = new Button(this);
		btn3.setText("Start OpenStreetMap");
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.openstreetmaps.org/?lat=49.452&lon=11.082&zoom=20");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		ll.addView(btn1);
		ll.addView(btn2);
		ll.addView(btn3);
		setContentView(ll);
	}
}