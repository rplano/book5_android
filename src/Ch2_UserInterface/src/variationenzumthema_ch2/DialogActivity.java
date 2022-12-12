package variationenzumthema_ch2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import variationenzumthema.ch2.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * DialogActivity
 *
 * This activity shows how to create a simple user dialog based on a string
 * array.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DialogActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_activity);

		Button aboutButton = (Button) this.findViewById(R.id.btn_new_game);
		aboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openNewGameDialog();
			}
		});
	}

	private String[] who_starts = { "Computer", "Human" };

	private void openNewGameDialog() {
		new AlertDialog.Builder(this).setTitle("Who should start?")
				.setItems(who_starts, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(((Dialog) dialog).getContext(),
								"Let’s start the game, " + which + " will start.", Toast.LENGTH_LONG).show();
					}
				}).show();
	}
}
