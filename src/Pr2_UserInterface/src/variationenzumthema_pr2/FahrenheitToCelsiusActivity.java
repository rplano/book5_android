package variationenzumthema_pr2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import variationenzumthema.pr2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * FahrenheitToCelsiusActivity
 * 
 * An activity converting Fahrenheit to Celsius demonstrating the use of the
 * SeekBar class.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class FahrenheitToCelsiusActivity extends Activity {

	private TextView tvFahrenheit;
	private TextView tvCelsius;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fahrenheit_to_celsius_activity);

		tvFahrenheit = (TextView) findViewById(R.id.fahrenheit);
		tvCelsius = (TextView) findViewById(R.id.celsius);

		SeekBar seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				double f = convertProgressToFahrenheit(progress);
				double c = fahrenheitToCelsius(f);

				tvFahrenheit.setText(String.format("%1$,.1f", f) + "°F");
				tvCelsius.setText("" + String.format("%1$,.1f", c) + "°C");
			}
		});

	}

	private int convertProgressToFahrenheit(int progress) {
		return (4 * progress) - 100;
	}

	private double fahrenheitToCelsius(double f) {
		double c = (5.0 / 9.0) * (f - 32.0);
		return c;
	}

}