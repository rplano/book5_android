package variationenzumthema_pr2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * WordGuess
 * 
 * Like hangman. You are shown empty spaces, and you can guess letters.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WordGuessActivity extends Activity {

	private String wordToGuess;
	private String wordShown;

	private TextView tvWordShown;
	private EditText etCharEntered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordguess_activity);

		tvWordShown = (TextView) findViewById(R.id.word_shown);
		etCharEntered = (EditText) findViewById(R.id.char_entered);

		Button btnCheckWord = (Button) findViewById(R.id.btn_check_word);
		btnCheckWord.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				char c = etCharEntered.getText().charAt(0);
				if (wordToGuess.indexOf(c) > -1) {
					String wordShownNew = "";
					for (int i = 0; i < wordToGuess.length(); i++) {
						if (wordToGuess.charAt(i) == c) {
							wordShownNew += c;
						} else {
							wordShownNew += wordShown.charAt(i);
						}
					}
					wordShown = wordShownNew;
					tvWordShown.setText(wordShown);
				}
			}
		});

		initWord();
	}

	private void initWord() {
		wordToGuess = pickRandomWord();
		wordShown = "";
		for (int i = 0; i < wordToGuess.length(); i++) {
			wordShown += "-";
		}
		tvWordShown.setText(wordShown);
	}

	private String pickRandomWord() {
		String[] words = { "dog", "fish", "chicken", "cat", "mother" };
		int index = nextRandomInt(0, words.length - 1);
		return words[index];
	}

	private int nextRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}
}
