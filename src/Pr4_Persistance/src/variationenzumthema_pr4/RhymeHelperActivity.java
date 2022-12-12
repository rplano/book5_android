package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Rhymes
 * 
 * Use the trie datastructure and a dictionary to find rhymes. <br/>
 * 1) read a dictionary from the file to fill the trie data structure with
 * values. You can either make english or german rhymes.<br/>
 * 2) before putting the words into the trie, reverse their order (why?)<br/>
 * 3) ask the user to enter a word. Take the last three letters of that word and
 * use the trie.nodesWithPrefix() method to find words that rhyme.<br/>
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class RhymeHelperActivity extends Activity {

	private EditText et;
	private TextView tv;

	private SimpleTrie trie = new SimpleTrie();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rhyme_helper_activity);

		et = (EditText) findViewById(R.id.edittext);

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String word = et.getText().toString();
				String rhymes = "";
				for (String s : trie.nodesWithPrefix(reverseString(word))) {
					if (s.length() < 7) {
						rhymes += reverseString(s) + "\n";
					}
				}
				tv.setText(rhymes);
			}
		});

		loadLexiconFromFile("dictionary_en_de.txt");
	}

	private String reverseString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = s.length() - 1; i >= 0; i--) {
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}

	/**
	 * This method should load the dictionary from file and store it in a trie.
	 * 
	 * @param fileName
	 */
	private void loadLexiconFromFile(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (true) {
				String words = br.readLine();
				if (words == null)
					break;
				StringTokenizer st = new StringTokenizer(words, "=");
				String en = st.nextToken();
				String de = st.nextToken();
				// maybe some of your code goes here
				trie.add(reverseString(en.toLowerCase()));
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
