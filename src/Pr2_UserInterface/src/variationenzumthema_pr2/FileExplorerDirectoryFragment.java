package variationenzumthema_pr2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * FileExplorerDirectoryFragment
 *
 * This fragment shows the directories.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FileExplorerDirectoryFragment extends ListFragment {

	private SelectionListener callback;
	private File[] dirs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dirs = getDirs(new File("/storage/emulated/0"));
		//dirs = getDirs(new File("/system/app"));

		String[] fileNames = new String[dirs.length];
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = dirs[i].getName();
		}
		if (fileNames != null) {
			Arrays.sort(fileNames);
		}

		setListAdapter(
				new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, fileNames));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (SelectionListener) activity;
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id) {
		callback.onDirSelected(dirs[position]);
	}

	private File[] getDirs(File file) {
		File[] dirs = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return dirs;
	}

}
