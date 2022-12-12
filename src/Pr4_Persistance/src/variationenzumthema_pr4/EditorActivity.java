package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import variationenzumthema.pr4.R;
import variationenzumthema_pr4.FileChooser.FileSelectedListener;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * EditorActivity
 *
 * This activity implements a simple text editor. It can read and write text
 * files.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class EditorActivity extends Activity implements FileSelectedListener {

	private EditText et;
	private FileChooser fc;

	private File currentFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_activity);

		et = (EditText) findViewById(R.id.editText);

		currentFile = android.os.Environment.getExternalStorageDirectory();
		fc = new FileChooser(this, currentFile.getAbsolutePath());
		fc.setFileListener(this);
	}

	public void fileSelected(File file) {
		currentFile = file;
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				sb.append(line + "\n");
			}
			br.close();
			et.setText(sb);

		} catch (Exception e) {
			Log.e("EditorActivity", e.getMessage());
		}
	}

	private void saveFile(String fileName) {
		currentFile = new File(fileName);
		try {
			String text = et.getText().toString();
			FileWriter fw = new FileWriter(currentFile);
			fw.write(text);
			fw.close();

		} catch (Exception e) {
			Log.e("EditorActivity", e.getMessage());
		}
	}

	private void openSaveFileDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Save");

		final EditText input = new EditText(this);
		input.setText(currentFile.getAbsolutePath());
		dialog.setView(input);

		dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String fileName = input.getText().toString();
				saveFile(fileName);
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_open_file:
			fc.showDialog();
			return true;

		case R.id.action_save_file:
			openSaveFileDialog();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
