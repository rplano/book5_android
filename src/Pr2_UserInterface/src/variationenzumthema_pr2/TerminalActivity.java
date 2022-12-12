package variationenzumthema_pr2;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.pr2.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * TerminalActivity
 * 
 * An activity lets you execute Linux shell commands.
 * 
 * @see https://www.learn2crack.com/2014/03/android-executing-shell-commands.html
 * @see https://github.com/offirgolan/Root-Helper
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TerminalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terminal_activity);

		final TextView tv = (TextView) findViewById(R.id.result);
		tv.setMovementMethod(new ScrollingMovementMethod());

		final AutoCompleteTextView et = (AutoCompleteTextView) findViewById(R.id.autocomplete);
		String[] commands = new String[] { "ps", "pwd", "cat /proc/cpuinfo", "ls /storage/emulated/0", "ls /system/app",
				"cd /storage/emulated/0", "whoami" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, commands);
		et.setAdapter(adapter);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String command = et.getText().toString();
				String output = execute(command);
				tv.setText(output);
			}
		});

	}

	public String execute(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			//Process root = Runtime.getRuntime().exec("su");
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			return e.toString();
		}
		return output.toString();
	}

}