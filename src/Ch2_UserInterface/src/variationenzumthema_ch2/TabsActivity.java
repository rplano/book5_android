package variationenzumthema_ch2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TabsActivity
 *
 * This activity shows how to use tabs.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TabsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_activity);

		// ui setup
		final TabHost host = (TabHost) findViewById(R.id.tabHost);
		host.setup();
		// Tab 0
		TabHost.TabSpec spec = host.newTabSpec("Tab Zero");
		spec.setContent(R.id.tab0);
		spec.setIndicator("Zeroth Tab");
		host.addTab(spec);
		// Tab 1
		spec = host.newTabSpec("Tab One");
		spec.setContent(R.id.tab1);
		spec.setIndicator("First Tab");
		host.addTab(spec);
		// Tab 2
		spec = host.newTabSpec("Tab Two");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Second Tab");
		host.addTab(spec);

		host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				showToast(host.getCurrentTab());
			}
		});
	}

	private void showToast(int currentTab) {
		Toast.makeText(this, "Tab" + currentTab + " was triggered.", Toast.LENGTH_SHORT).show();
	}
}
