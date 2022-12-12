package variationenzumthema_pr2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * CrosswordPuzzle
 * 
 * Creates crossword puzzles from a given set of initial words. It starts by
 * filling random words horizontally and then uses a trie data structure to find
 * words that fit vertically. This is also a randomized algorithm.
 * 
 * Crossword directories are available here: <br/>
 * http://www.onlinecrosswords.net/crossworddirectory.php
 * https://crosswordlabs.com/ http://www.dowedo.net/compiler/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CrosswordPuzzleActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "CrosswordPuzzleActivity";

	private final int PUZZLE_SIZE = 9;

	private int width = -1;
	private int height = -1;
	private EditText et;
	private Button[] btns;
	Random rgen = new Random();

	private char[][] puzzle = new char[PUZZLE_SIZE][PUZZLE_SIZE];
	private int[][] verBgColors = new int[PUZZLE_SIZE][PUZZLE_SIZE];
	private int[][] horBgColors = new int[PUZZLE_SIZE][PUZZLE_SIZE];

	private SimpleTrie trie = new SimpleTrie();
	private Set<String> finds = new HashSet<String>();

	private final String[] words = { "ark", "card", "dad", "day", "dear", "down", "east", "erase", "ever", "father",
			"itsy", "man", "mesh", "near", "nerf", "send", "stinks", "stun", "sync", "yard" };
	private final String[] hints = { "Arche", "Karte", "Papa", "Tag", "lieb", "Daunen", "Ost", "löschen", "jemals",
			"Vater", "winzig", "Mann", "Gitter", "nahe", "Nerf", "senden", "stinkt", "betäuben", "synchronisieren",
			"Garten" };

	private boolean verticalMode = true;
	private Map<Integer, Integer> verticalHints = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> horizontalHints = new HashMap<Integer, Integer>();
	private Map<String, Integer> reverseLookup = new HashMap<String, Integer>();

	private int curX, curY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		for (int i = 0; i < words.length; i++) {
			reverseLookup.put(words[i], i);
		}

		initArrayWithSpaces();

		loadWordsIntoTrie();

		initPuzzleWithWords();

		findVerticals(6);
		findVerticals(5);
		findVerticals(4);
		// findVerticals(3);

		createUI();
	}

	private void checkIfCorrect(String guess) {
		Log.i(TAG, "checkIfCorrect()  text=" + guess);
		int i = curX + curY * PUZZLE_SIZE;
		Integer idx;
		if (verticalMode) {
			idx = verticalHints.get(i);
		} else {
			idx = horizontalHints.get(i);
		}

		if (idx != null) {
			String text = words[idx];

			if (text != null && text.equals(guess.toLowerCase())) {
				// Log.i(TAG, "guess correct! text=" + guess);
				if (verticalMode) {
					for (int k = 0; k < text.length(); k++) {
						int pos = i + k * PUZZLE_SIZE;
						btns[pos].setText("" + text.charAt(k));
					}
					et.setText("");

				} else {
					for (int k = 0; k < text.length(); k++) {
						int pos = i + k;
						btns[pos].setText("" + text.charAt(k));
					}
					et.setText("");
				}
			} else {
				Toast.makeText(this, "Not correct!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Touch the beginning of a word!", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		Log.i(TAG, "btn.getTag()=" + btn.getTag());
		int i = (int) btn.getTag();
		curX = i % PUZZLE_SIZE;
		curY = i / PUZZLE_SIZE;

		if (verticalMode) {
			if (verBgColors[curX][curY] == 0) {
				switchMode();
			} else {
				showHint(i);
			}
		} else {
			if (horBgColors[curX][curY] == 0) {
				switchMode();
			} else {
				showHint(i);
			}
		}
	}

	private void showHint(int i) {
		Integer idx;
		if (verticalMode) {
			idx = verticalHints.get(i);
		} else {
			idx = horizontalHints.get(i);
		}
		if (idx != null) {
			String text = hints[idx];
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		}
	}

	private void switchMode() {
		verticalMode = !verticalMode;
		for (int i = 0; i < PUZZLE_SIZE * PUZZLE_SIZE; i++) {
			if (verticalMode) {
				btns[i].setBackgroundColor(verBgColors[i % PUZZLE_SIZE][i / PUZZLE_SIZE]);
			} else {
				btns[i].setBackgroundColor(horBgColors[i % PUZZLE_SIZE][i / PUZZLE_SIZE]);
			}
		}
	}

	/**
	 * we add verticals in all-caps, we should still check for duplicates.
	 */
	private void findVerticals(int length) {
		// pick 100 random x,y values and see what happens
		for (int i = 0; i < 100; i++) {
			int x = (int) (Math.random() * PUZZLE_SIZE);
			int y = (int) (Math.random() * (PUZZLE_SIZE - length + 1));
			String key = "";// "d..n";
			for (int j = 0; j < length; j++) {
				key += puzzle[x][y + j];
			}

			for (String s : trie.nodesThatMatch(key.toLowerCase())) {
				if (s.length() <= length) {
					finds.add(s + ",x=" + x + ",y=" + y);
					// add to puzzle in all-caps
					int idx = reverseLookup.get(s);
					// verticalHints.put((x + y * PUZZLE_SIZE), hints[idx]);
					verticalHints.put((x + y * PUZZLE_SIZE), idx);
					int color = rgen.nextInt();
					for (int j = 0; j < s.length(); j++) {
						puzzle[x][y + j] = s.toUpperCase().charAt(j);
						verBgColors[x][y + j] = color;
					}
				}
			}
		}

	}

	private void loadWordsIntoTrie() {
		for (int i = 0; i < words.length; i++) {
			trie.add(words[i]);
		}
	}

	private void initPuzzleWithWords() {
		addWordToPuzzle(2, 0, 0, true);
		addWordToPuzzle(15, 5, 0, true);
		addWordToPuzzle(6, 2, 1, true);
		addWordToPuzzle(10, 5, 2, true);
		addWordToPuzzle(14, 0, 3, true);
		addWordToPuzzle(0, 3, 4, true);
		addWordToPuzzle(18, 5, 5, true);
		addWordToPuzzle(12, 0, 6, true);
		addWordToPuzzle(8, 3, 7, true);
		addWordToPuzzle(13, 0, 8, true);
	}

	/**
	 * 
	 * @param string
	 * @param i
	 * @param b
	 *            if true horizontal, else vertical
	 */
	private void addWordToPuzzle(int idx, int x, int y, boolean horizontal) {
		String word = words[idx];
		int color = rgen.nextInt();
		if (horizontal) {
			// horizontalHints.put((x + y * PUZZLE_SIZE), hints[idx]);
			horizontalHints.put((x + y * PUZZLE_SIZE), idx);
			for (int i = 0; i < word.length(); i++) {
				puzzle[x + i][y] = word.charAt(i);
				horBgColors[x + i][y] = color;
			}
		} else {
			// verticalHints.put((x + y * PUZZLE_SIZE), hints[idx]);
			verticalHints.put((x + y * PUZZLE_SIZE), idx);
			for (int i = 0; i < word.length(); i++) {
				puzzle[x][y + i] = word.charAt(i);
				verBgColors[x][y + i] = color;
			}
		}

	}

	private void initArrayWithSpaces() {
		for (int i = 0; i < puzzle.length; i++) {
			Arrays.fill(puzzle[i], '.');
		}
	}

	private void createUI() {
		LinearLayout ll1 = new LinearLayout(this);
		ll1.setOrientation(LinearLayout.VERTICAL);
		ll1.setBackgroundColor(0x200000ff);
		ll1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(ll1);

		getScreenDimensions();

		// tv = (TextView) findViewById(R.id.textView2);
		et = new EditText(this);
		et.setHint("Enter entry here...");
		et.setGravity(Gravity.LEFT);
		et.setTextSize(24);
		et.setLines(1);
		et.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et.setInputType(InputType.TYPE_CLASS_TEXT);
		et.setBackgroundColor(0xffe0e0e0);
		et.setPadding(5, 5, 5, 5);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		et.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				// Log.i(TAG, "actionId=" + actionId);
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// Log.i(TAG, "v.getText()=" + v.getText());
					checkIfCorrect(v.getText().toString());
					// make keyboard disappear
					InputMethodManager mgr = (InputMethodManager) getSystemService(
							getApplicationContext().INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(et.getWindowToken(), 0);
					handled = true;
				}
				return handled;
			}
		});
		ll1.addView(et);

		// GridLayout gl = (GridLayout) findViewById(R.id.gridLayout1);
		GridLayout gl = new GridLayout(this);
		gl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gl.removeAllViews();
		gl.setColumnCount(PUZZLE_SIZE);
		gl.setRowCount(PUZZLE_SIZE);
		ll1.addView(gl);

		btns = new Button[PUZZLE_SIZE * PUZZLE_SIZE];
		for (int i = 0; i < PUZZLE_SIZE * PUZZLE_SIZE; i++) {
			btns[i] = new Button(this);
			btns[i].setTag(i);
			// btns[i].setText("" + puzzle[i % PUZZLE_SIZE][i / PUZZLE_SIZE]);
			btns[i].setText(" ");
			if (verticalMode) {
				btns[i].setBackgroundColor(verBgColors[i % PUZZLE_SIZE][i / PUZZLE_SIZE]);
			} else {
				btns[i].setBackgroundColor(horBgColors[i % PUZZLE_SIZE][i / PUZZLE_SIZE]);
			}
			btns[i].setLayoutParams(getLayoutParams(width, i));
			btns[i].setOnClickListener(this);
			gl.addView(btns[i]);
		}
	}

	private void getScreenDimensions() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	}

	private GridLayout.LayoutParams getLayoutParams(int width, int i) {
		GridLayout.LayoutParams param = new GridLayout.LayoutParams();
		// param.height = LayoutParams.WRAP_CONTENT;
		// param.width = LayoutParams.WRAP_CONTENT;
		// param.rightMargin = 5;
		// param.topMargin = 5;
		param.width = width / PUZZLE_SIZE;
		param.setGravity(Gravity.CENTER);
		param.columnSpec = GridLayout.spec(i % PUZZLE_SIZE);
		param.rowSpec = GridLayout.spec(i / PUZZLE_SIZE);
		return param;
	}

}
