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
 * CreditCard
 * 
 * The Luhn algorithm is used to see if credit card numbers contain errors.
 * 
 * Test these: 49927398716, 1234567812345670, 79927398713
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class CreditCardActivity extends Activity {
	
	private EditText creditCardET;
	private TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_card_activity);

		creditCardET = (EditText) findViewById(R.id.editText);
		result = (TextView) findViewById(R.id.result);

		Button btnCheck = (Button) findViewById(R.id.button);
		btnCheck.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String creditCardNumber = creditCardET.getText().toString();
				boolean correct = checkCreditCardNumber(creditCardNumber);
				if (correct) {
					result.setText("Number is correct.");
				} else {
					result.setText("You got screwed!");
				}
			}
		});

	}

	private boolean checkCreditCardNumber(String creditCardNumber) {
		if (creditCardNumber.length() != 16) {
			return false;
		}
		int sum = 0;
		int len = creditCardNumber.length();
		for (int i = 0; i < len; i++) {
			int x = creditCardNumber.charAt(i) - '0'; // turn char in to int
			int y = x * (2 - (i + len) % 2); // multiply by two every other
			if (y > 9) {
				y -= 9;
			}
			sum += y;
		}
		return sum % 10 == 0;
	}
}
