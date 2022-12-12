package variationenzumthema_pr2;

import java.io.File;
import java.util.Arrays;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * FileExplorerFileFragment
 *
 * This fragment shows the files in a TextViews.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FileExplorerFileFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fileexplorer_files, container, false);
	}

	public void updateFilesDisplay(File dirName) {
		TextView mTextView = (TextView) getView().findViewById(R.id.fileexplorer_files_tv);
		String[] files = getAllFilesAndDirs(dirName);
		StringBuffer sb = new StringBuffer();
		// sb.append(".." + "\n");
		if (files != null) {
			for (String file : files) {
				sb.append(file + "\n");
			}
		}
		mTextView.setText(sb.toString());
	}

	private String[] getAllFilesAndDirs(File file) {
		String[] filesAndDirs = file.list();
		if (filesAndDirs != null) {
			Arrays.sort(filesAndDirs);
		}
		return filesAndDirs;
	}
}
