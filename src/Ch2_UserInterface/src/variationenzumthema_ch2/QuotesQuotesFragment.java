package variationenzumthema_ch2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * QuotesQuotesFragment
 *
 * This fragment shows the quotes.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class QuotesQuotesFragment extends Fragment {

	private String[] quotes;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.quotes_quotes, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (quotes == null) {
			quotes = getResources().getStringArray(R.array.Quotes);
		}
	}

	public void updateQuotesDisplay(int position) {
		TextView tv = (TextView) getView().findViewById(R.id.quotes_view);
		tv.setText(quotes[position]);
	}
}