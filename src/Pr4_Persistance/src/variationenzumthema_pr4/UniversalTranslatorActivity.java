package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import variationenzumthema.pr4.R;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * UniversalTranslatorActivity
 * 
 * Translates a sentence from English into one of 14 languages.
 * 
 * @see http://introcs.cs.princeton.edu/java/data/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class UniversalTranslatorActivity extends Activity {

	private EditText et;
	private TextView tv;

	private Map<String, List<String>> dictionary = new TreeMap<String, List<String>>();
	private List<String> languages;
	private List<String> words;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.universal_translator_activity);

		readDictionaryFromFile();

		et = (EditText) findViewById(R.id.edittext);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
		Spinner sp = (Spinner) findViewById(R.id.spinner);
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// Log.i("UniversalTranslatorActivity", "position="+position);
				tv.setText(translateToLanguageNr(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});

		tv = (TextView) findViewById(R.id.textview);
	}

	private void readDictionaryFromFile() {
		try {
			InputStream is = getAssets().open("Languages.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
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

	private String translateToLanguageNr(int language) {
		String translation = "";
		String englishSentence = et.getText().toString();
		StringTokenizer st = new StringTokenizer(englishSentence, " ,.?!-");
		while (st.hasMoreTokens()) {
			String word = st.nextToken().trim().toLowerCase();
			List<String> tmpWords = dictionary.get(word);
			if (tmpWords != null) {
				String trans = tmpWords.get(language);
				if (trans != null && trans.length() > 0) {
					translation += trans + " ";
				} else {
					translation += word + " ";
				}
			} else { 
				translation += word + " ";
			}
		}
		return translation;
	}
}