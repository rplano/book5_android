package variationenzumthema_pr2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Blackjack
 * 
 * Computer creates a random number between 17 and 25. The player is dealt a
 * two-card hand and adds together the value of the cards. Whoever has a higher
 * score that is less than 21 wins. Otherwise it is a draw. The player gets
 * cards between 1 and 11.
 * 
 * @see https://en.wikipedia.org/wiki/Blackjack
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BlackjackActivity extends Activity {

	private List<Integer> cards = new ArrayList<Integer>();

	private TextView tvPlayerCards;
	private TextView tvDealerCards;
	private TextView tvWinOrLoose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blackjack_activity);

		tvPlayerCards = (TextView) findViewById(R.id.player);
		tvDealerCards = (TextView) findViewById(R.id.dealer);
		tvWinOrLoose = (TextView) findViewById(R.id.win_or_loose);

		Button btnHit = (Button) findViewById(R.id.btn_hit);
		btnHit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cards.add(nextRandomInt(1, 11));
				showPlayerCards();
			}
		});

		Button btnStand = (Button) findViewById(R.id.btn_stand);
		btnStand.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				checkResult();
			}
		});

		// init player cards
		cards.add(nextRandomInt(1, 11));
		cards.add(nextRandomInt(1, 11));
		showPlayerCards();
	}

	private void showPlayerCards() {
		String playerCards = "Your cards:\n";
		for (int card : cards) {
			playerCards += card + ", ";
		}
		tvPlayerCards.setText(playerCards);
	}

	public void checkResult() {
		int playerScore = getPlayerScore();
		int dealerScore = nextRandomInt(17, 25);
		tvDealerCards.setText("Dealers cards: " + dealerScore);

		String result = "";
		if (playerScore > 21) {
			result = ("House wins.");
		} else if (dealerScore > 21) {
			result = ("You win.");
		} else if (playerScore > dealerScore) {
			result = ("You win.");
		} else if (playerScore == dealerScore) {
			result = ("Tie.");
		} else {
			result = ("House wins.");
		}
		tvWinOrLoose.setText(result);
	}

	private int getPlayerScore() {
		int score = 0;
		for (int card : cards) {
			score += card;
		}
		return score;
	}

	private int nextRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}
}
