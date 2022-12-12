package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Languages
 * 
 * Translates from English into 14 languages.
 * 
 * @see http://introcs.cs.princeton.edu/java/data/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class LanguagesActivity extends Activity {

	private EditText et;
	private ListView lv;

	private Map<String, List<String>> dictionary = new TreeMap<String, List<String>>();
	private List<String> languages;
	private List<String> words;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.languages_activity);

		et = (EditText) findViewById(R.id.editText);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String english = et.getText().toString();
				List<String> tmpWords = dictionary.get(english);
				words = new ArrayList<String>();
				for (int i = 0; i < tmpWords.size(); i++) {
					String word = languages.get(i) + ": " + tmpWords.get(i);
					words.add(word);
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
						android.R.layout.simple_spinner_item, words);
				lv.setAdapter(adapter);
			}
		});

		readDictionaryFromFile();

		lv = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, words);
		lv.setAdapter(adapter);
	}

	private void readDictionaryFromFile() {
		try {
			InputStream is = getAssets().open("Languages.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			// first line contains languages:
			String languageLine = br.readLine();
			languages = parseLine(languageLine);
			words = parseLine(languageLine);

			// next lines contain word tuplets:
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				List<String> translations = parseLine(line);
				dictionary.put(translations.get(0), translations);
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> parseLine(String line) {
		List<String> translations = new ArrayList<String>();
		while (true) {
			int begin = line.indexOf("\"");
			if (begin < 0)
				break;
			int end = line.indexOf("\"", begin + 1);
			if (end < 0) {
				System.out.println("***** this should never happen! *****");
			}
			String s = line.substring(begin + 1, end);
			line = line.substring(end + 1);
			translations.add(s);
		}
		return translations;
	}

}