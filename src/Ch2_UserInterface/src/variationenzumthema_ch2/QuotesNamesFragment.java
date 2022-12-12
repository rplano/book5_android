package variationenzumthema_ch2;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * QuotesNamesFragment
 *
 * This fragment shows the names.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class QuotesNamesFragment extends ListFragment {

	private SelectionListener callback;
	private String[] names;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		names = getResources().getStringArray(R.array.Names);
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, names));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (SelectionListener) activity;
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id) {
		callback.onItemSelected(position);
	}
}
