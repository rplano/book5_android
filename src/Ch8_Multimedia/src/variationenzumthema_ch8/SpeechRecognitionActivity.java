package variationenzumthema_ch8;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import variationenzumthema.ch8.R;

import java.util.ArrayList;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * SpeechRecognitionActivity
 *
 * This activity shows how to do simple speech recognition.
 *
 * Make sure voice search is working, if needed install first (you may need to
 * reboot)
 * 
 * @see Android; How to implement voice recognition, a nice easy tutorial,
 *      www.jameselsey.co.uk/blogs/techblog/android-how-to-implement-voice-recognition-a-nice-easy-tutorial/
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class SpeechRecognitionActivity extends Activity {
	private final int REQUEST_CODE = 333;

	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speech_recognition_activity);

		tv = (TextView) findViewById(R.id.textView);

		Button speakButton = (Button) findViewById(R.id.speakButton);
		speakButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

				startActivityForResult(intent, REQUEST_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				tv.setText(matches.get(0));
			} else {
				tv.setText("Something went wrong: " + resultCode);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}