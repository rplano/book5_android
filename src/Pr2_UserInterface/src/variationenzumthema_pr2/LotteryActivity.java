package variationenzumthema_pr2;

import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * Lottery
 * 
 * Generate 6 unique random numbers (no duplicates) between 1 and 49 using a Set.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class LotteryActivity extends Activity {
	
	private TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lottery_activity);

		result = (TextView) findViewById(R.id.result);

		Button btnCreateLuckyNumbers = (Button) findViewById(R.id.button);
		btnCreateLuckyNumbers.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String numbers = generateLotteryNumbers();
				result.setText("Your lucky numbers are:\n" + numbers);
			}
		});
	}

	private String generateLotteryNumbers() {
		Set<Integer> nrs = new TreeSet<Integer>();

		while (nrs.size() < 6) {
			nrs.add(nextRandomInt(1,49));
		}

		StringBuffer numbers = new StringBuffer();
		for (int nr : nrs) {
			numbers.append(nr + ", ");
		}
		numbers.deleteCharAt(numbers.length()-1);	// OBOB
		numbers.deleteCharAt(numbers.length()-1);
		return numbers.toString();
	}

	private int nextRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}
}
