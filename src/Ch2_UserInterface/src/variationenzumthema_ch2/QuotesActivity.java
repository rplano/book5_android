package variationenzumthema_ch2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * QuotesActivity
 *
 * This activity demonstrates how fragments are being used. It uses quotes of
 * famous people as an example.
 *
 * @see https://developer.android.com/guide/components/fragments.html
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class QuotesActivity extends Activity implements SelectionListener {

	private QuotesNamesFragment namesFragment;
	private QuotesQuotesFragment quotesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.quotes_activity);

		Log.i("QuotesActivity", "isInSinglePaneMode="+isInSinglePaneMode());
		if (isInSinglePaneMode()) {
			quotesFragment = new QuotesQuotesFragment();
			namesFragment = new QuotesNamesFragment();

			FragmentManager mFragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, namesFragment);
			fragmentTransaction.commit();

		} else {
			quotesFragment = (QuotesQuotesFragment) getFragmentManager().findFragmentById(R.id.quotes_fragment);
		}
	}

	private boolean isInSinglePaneMode() {
		return findViewById(R.id.fragment_container) != null;
	}

	public void onItemSelected(int position) {
		if (isInSinglePaneMode()) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, quotesFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

			getFragmentManager().executePendingTransactions();
		}
		quotesFragment.updateQuotesDisplay(position);
	}
}
