package variationenzumthema_pr2;

import java.io.File;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * FileExplorerActivity
 *
 * This activity demonstrates how fragments are being used. It implements a
 * simple file explorer.
 *
 * @see https://developer.android.com/guide/components/fragments.html
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FileExplorerActivity extends Activity implements SelectionListener {

	private FileExplorerDirectoryFragment dirFragment;
	private FileExplorerFileFragment fileFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fileexplorer_activity);

		Log.i("QuotesActivity", "isInSinglePaneMode=" + isInSinglePaneMode());
		if (isInSinglePaneMode()) {
			fileFragment = new FileExplorerFileFragment();
			dirFragment = new FileExplorerDirectoryFragment();

			FragmentManager mFragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fileexplorer_activity_portrait, dirFragment);
			fragmentTransaction.commit();

		} else {
			fileFragment = (FileExplorerFileFragment) getFragmentManager()
					.findFragmentById(R.id.fileexplorer_fragment_file);
		}
	}

	private boolean isInSinglePaneMode() {
		return findViewById(R.id.fileexplorer_activity_portrait) != null;
	}

	@Override
	public void onDirSelected(File dirName) {
		if (isInSinglePaneMode()) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fileexplorer_activity_portrait, fileFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

			getFragmentManager().executePendingTransactions();
		}
		fileFragment.updateFilesDisplay(dirName);
	}
}
