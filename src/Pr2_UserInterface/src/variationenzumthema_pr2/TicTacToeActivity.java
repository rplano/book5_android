package variationenzumthema_pr2;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr2.R;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * TicTacToe
 * 
 * Implements a simple version of the TicTacToe game.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TicTacToeActivity extends Activity implements View.OnClickListener {
	// private static final String TAG = "TicTacToeActivity";

	private final int NR_OF_COLUMNS = 3;
	private final int NR_OF_ROWS = 3;

	private TicTacToeLogic logic;
	private int currentPlayer = 1;
	private boolean gameOver = false;

	private int width = -1;
	private int height = -1;

	private ImageButton[] imgBtns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tictactoe_activity);

		GridLayout gl = (GridLayout) findViewById(R.id.gl_tictactoe);
		gl.removeAllViews();
		gl.setColumnCount(NR_OF_COLUMNS);
		gl.setRowCount(NR_OF_ROWS);

		logic = new TicTacToeLogic();

		getScreenDimensions();

		// Button[] btns = new Button[btnLabels.length];
		imgBtns = new ImageButton[NR_OF_COLUMNS * NR_OF_ROWS];
		for (int i = 0; i < imgBtns.length; i++) {
			imgBtns[i] = new ImageButton(this);
			imgBtns[i].setId(i);
			imgBtns[i].setAdjustViewBounds(true);
			imgBtns[i].setMaxWidth(width / 3);
			imgBtns[i].setMaxHeight(width / 3);
			imgBtns[i].setImageResource(R.drawable.tictactoe_);
			imgBtns[i].setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
			imgBtns[i].setOnClickListener(this);
			gl.addView(imgBtns[i]);
		}
	}

	private void getScreenDimensions() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	}

	@Override
	public void onClick(View v) {
		if (!gameOver) {
			int id = ((ImageButton) v).getId();

			int i = id % NR_OF_COLUMNS;
			int j = id / NR_OF_COLUMNS;
			// Log.i(TAG, "id=" + id + ", i=" + i + ", j=" + j);

			if (logic.isMoveAllowed(currentPlayer, i, j)) {
				displayPlayer(id);

				if (logic.isGameOver()) {
					displayGameOver();
				} else {
					computerMakesMove();
				}

			} else {
				Toast.makeText(this, "Move not allowed!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void computerMakesMove() {
		int i, j, id; // ugly as hell
		// pick random spot
		do {
			id = nextRandomInt(0, 8);
			i = id % NR_OF_COLUMNS;
			j = id / NR_OF_COLUMNS;
		} while (!logic.isMoveAllowed(currentPlayer, i, j));

		displayPlayer(id);

		if (logic.isGameOver()) {
			displayGameOver();
		}
	}

	private void displayGameOver() {
		gameOver = true;
		if (currentPlayer == 2) {
			Toast.makeText(this, "You won!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Computer won!", Toast.LENGTH_LONG).show();
		}
	}

	private void displayPlayer(int id) {
		if (currentPlayer == 1) {
			imgBtns[id].setImageResource(R.drawable.tictactoe_x);
			imgBtns[id].setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
			currentPlayer = 2;
		} else {
			imgBtns[id].setImageResource(R.drawable.tictactoe_o);
			imgBtns[id].setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
			currentPlayer = 1;
		}
	}

	private int nextRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}
}
