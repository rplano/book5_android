package variationenzumthema_pr7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MainActivity
 *
 * This activity allows you to select among the activities in this project.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final String TITLE = "Projects 7";

	private final String[] activityNames = { "NetworkScanner", "AllMyIPs", "TimeClientNIST", "JSONBooks", "GSONCities",
			"WebServer", "FileServer", "TCPChat", "UDPChat", "GameServer", "GameClient", "GameClient2", "BTDiscovery",
			"BTChatServer", "BTChatClient", "BLEScanner", "BLEBeaconFinder", "BLETurnOnBT", "NFC", "CellTower",
			"WifiScanner", "IndoorLocation", "HotSpot", "RemoteDesktopClient", "TicTacToe" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create UI
		LinearLayout ll = createUILayout();

		// dynamically create buttons from activityNames[] and add
		// OnClickListener
		for (int i = 0; i < activityNames.length; i++) {
			Button btn = new Button(this);
			btn.setText(activityNames[i]);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						String classNameOfButton = ((Button) v).getText().toString();
						Intent i = new Intent(getApplicationContext(), Class.forName(
								this.getClass().getPackage().getName() + "." + classNameOfButton + "Activity"));
						startActivity(i);
					} catch (ClassNotFoundException e) {
						Log.e(TAG, "onClick(): " + e);
					}
				}
			});
			ll.addView(btn);
		}

		// add exit button
		Button btn = new Button(this);
		btn.setText("Exit");
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ll.addView(btn);
	}

	private LinearLayout createUILayout() {
		LinearLayout ll1 = new LinearLayout(this);
		ll1.setOrientation(LinearLayout.VERTICAL);
		ll1.setBackgroundColor(0x200000ff);
		ll1.setPadding(30, 30, 30, 30);
		ll1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(ll1);

		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll1.addView(sv);

		LinearLayout ll2 = new LinearLayout(this);
		ll2.setOrientation(LinearLayout.VERTICAL);
		ll2.setGravity(Gravity.CENTER);
		ll2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		sv.addView(ll2);

		TextView tv = new TextView(this);
		tv.setText(TITLE);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(24);
		tv.setPadding(25, 25, 25, 25);
		tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ll2.addView(tv);

		return ll2;
	}
}