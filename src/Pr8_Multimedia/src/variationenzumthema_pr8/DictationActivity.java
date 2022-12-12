package variationenzumthema_pr8;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * DictationActivity
 *
 * This activity turns spoken words into written text.
 *
 * @see http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DictationActivity extends Activity implements RecognitionListener {

	private TextView tv;
	private SpeechRecognizer recognizer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictation_activity);

		tv = (TextView) findViewById(R.id.textView);

		// init speech recognition engine
		if (SpeechRecognizer.isRecognitionAvailable(this)) {
			recognizer = SpeechRecognizer.createSpeechRecognizer(this);
			recognizer.setRecognitionListener(this);

			final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

			ToggleButton btn = (ToggleButton) findViewById(R.id.toggleButton);
			btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						recognizer.startListening(recognizerIntent);
					} else {
						recognizer.stopListening();
					}
				}
			});

		} else {
			Toast.makeText(this, "No speech recognition engine installed!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		if (recognizer != null) {
			recognizer.destroy();
			recognizer = null;
		}
		super.onPause();
	}

	@Override
	public void onPartialResults(Bundle results) {
		addResultsToTextView(results);
	}

	@Override
	public void onResults(Bundle results) {
		addResultsToTextView(results);
	}

	private void addResultsToTextView(Bundle results) {
		ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		String text = "";
		for (String match : matches) {
			text += match + "\n";
		}
		tv.append("- " + text + "\n");
	}

	@Override
	public void onError(int error) {
		Log.i("DictationActivity", "onError: " + error);
	}

	@Override
	public void onBeginningOfSpeech() {
	}

	@Override
	public void onEndOfSpeech() {
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
	}

	@Override
	public void onRmsChanged(float rmsdB) {
	}

}
