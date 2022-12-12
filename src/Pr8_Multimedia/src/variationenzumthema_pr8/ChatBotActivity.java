package variationenzumthema_pr8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import Eliza.ElizaMain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ChatBotActivity
 *
 * This activity turns ELIZA into a chatter box.
 *
 * The original ELIZA was described by Joseph Weizenbaum in Communications of
 * the ACM in January 1966.
 *
 * Eliza v0.1 written by Charles Hayden chayden@monmouth.com
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ChatBotActivity extends Activity {
	private final int REQUEST_CODE = 333;

	private TextView tv;

	private ElizaMain eliza;
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatbot_activity);

		tv = (TextView) this.findViewById(R.id.textView);

		initEliza();

		String me = "Hello";
		String eliz = eliza.processInput(me);
		tv.setText("ELIZA: "+eliz + "\n");

		initTTS(eliz);
	}

	private void talkToEliza(String me) {
		tv.append("You: "+me + "\n");
		String eliz = eliza.processInput(me);
		tts.speak(eliz, TextToSpeech.QUEUE_FLUSH, null, eliz);
		tv.append("ELIZA: "+eliz + "\n");
	}

	private void initTTS(final String msg) {
		tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					tts.setLanguage(Locale.US);
					setProgressListener();

					tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, msg);

				} else {
					Toast.makeText(getApplicationContext(),
							"No speech engine available.\nDid you install 'Google Text-to-speech'?", Toast.LENGTH_LONG)
							.show();
					tts.shutdown();
				}
			}
		});
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	private void setProgressListener() {
		tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

			@Override
			public void onDone(String utteranceId) {
				Log.i("ChatBotActivity", "onDone" + utteranceId);
				startListening();
			}

			@Override
			public void onStart(String utteranceId) {
			}

			@Override
			public void onError(String utteranceId) {
			}

		});
	}

	private void startListening() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			talkToEliza(matches.get(0));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initEliza() {
		eliza = new ElizaMain();
		try {
			eliza.readScript(getAssets().open("script.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
