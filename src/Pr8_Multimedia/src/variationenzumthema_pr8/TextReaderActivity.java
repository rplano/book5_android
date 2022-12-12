package variationenzumthema_pr8;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TextReaderActivity
 *
 * This activity reads the contents of an EditText using the text-to-speech
 * engine.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TextReaderActivity extends Activity {

	private TextToSpeech tts;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.text_reader_activity);

		final EditText et = (EditText) findViewById(R.id.edittext);

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tts.speak(et.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
			}
		});

		tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.ERROR) {
					Toast.makeText(context, "No speech engine available.\nDid you install 'Google Text-to-speech'?",
							Toast.LENGTH_LONG).show();
					tts.shutdown();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onPause();
	}
}
