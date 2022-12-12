package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

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
 * HexEditorActivity
 *
 * This activity implements a simple hex editor. It can read and write binary
 * files. You should be careful with writing, it might break things...
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HexEditorActivity extends Activity implements FileSelectedListener {

	private static final int NR_OF_BYTES_PER_LINE = 8;
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private EditText et;
	private FileChooser fc;

	private File currentFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hex_editor_activity);

		et = (EditText) findViewById(R.id.editText);

		// currentFile = new File("/storage/emulated/0");
		currentFile = android.os.Environment.getExternalStorageDirectory();
		fc = new FileChooser(this, currentFile.getAbsolutePath());
		fc.setFileListener(this);
	}

	public void fileSelected2(File file) {
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
			Log.e("HexEditorActivity", e.getMessage());
		}
	}

	public void fileSelected(File file) {
		try {
			// open file
			FileInputStream fis = new FileInputStream(file);

			StringBuffer sbHex = new StringBuffer();
			StringBuffer sbAscii = new StringBuffer();
			int count = 0;
			// read from file, line by line
			int content;
			while ((content = fis.read()) != -1) {
				if (content > 31 && content < 127) {
					sbAscii.append((char) content);
				} else {
					sbAscii.append(' ');
				}

				byte b = (byte) content;
				char c1 = DIGITS[(b >> 4) & 0xf];
				char c2 = DIGITS[b & 0xf];
				sbHex.append(c1);
				sbHex.append(c2);
				sbHex.append(' ');

				count++;
				if (count == NR_OF_BYTES_PER_LINE) {
					sbHex.append("   " + sbAscii + "\n");
					sbAscii = new StringBuffer();
					count = 0;
				}
			}

			// close file
			fis.close();

			// show file content
			// display.setText(sbHex.toString());
			et.setText(sbHex.toString());

		} catch (Exception e) {
			// System.out.println("File does not exist!");
			Log.e("HexEditorActivity", e.getMessage());
		}
	}

	private void saveFile(String fileName) {
		currentFile = new File(fileName);
		try {
			// open file
			FileOutputStream fw = new FileOutputStream(currentFile);

			// write to file
			// String text = display.getText();
			String text = et.getText().toString();
			StringTokenizer st = new StringTokenizer(text, "\n");
			while (st.hasMoreTokens()) {
				String line = st.nextToken();
				if (line.length() > NR_OF_BYTES_PER_LINE * 3) {
					line = line.substring(0, NR_OF_BYTES_PER_LINE * 3);
				}
				String[] hexs = line.split(" ");
				for (int i = 0; i < hexs.length; i++) {
					// System.out.print(hexs[i] + ",");
					byte b = (byte) Integer.parseInt(hexs[i], 16);
					// System.out.print(b + ",");
					fw.write(b);
				}
				// System.out.println();
			}

			// close file
			fw.close();

		} catch (Exception e) {
			// System.out.println("An error occurred!" + e);
			Log.e("HexEditorActivity", e.getMessage());
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
