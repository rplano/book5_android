package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * SpellChecker
 * 
 * First loads a lot of English words into a set, and then simply checks if the
 * searched word is in the set.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SpellCheckerActivity extends Activity {

	private EditText et;
	private Set<String> words;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spellchecker_activity);

		et = (EditText) findViewById(R.id.editText);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String word = et.getText().toString();
				if (words.contains(word.toLowerCase())) {
					Toast.makeText(v.getContext(), "Spelling is correct", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(v.getContext(), "Spelling is NOT correct", Toast.LENGTH_SHORT).show();
				}
			}
		});

		words = buildIndexFromFile("dictionary_en_de.txt");
	}

	private Set<String> buildIndexFromFile(String fileName) {
		HashSet<String> al = new HashSet<String>();
		try {
			InputStream is = getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				StringTokenizer st = new StringTokenizer(line, "=");
				String en = st.nextToken().trim().toLowerCase();
				String de = st.nextToken().trim().toLowerCase();
				al.add(en);
			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return al;
	}
}