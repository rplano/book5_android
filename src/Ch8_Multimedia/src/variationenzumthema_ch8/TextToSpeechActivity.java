package variationenzumthema_ch8;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * TextToSpeechActivity
 *
 * This activity shows how to use the text-to-speech engine of Android.
 *
 * In order to enable Google’s text to speech, go to settings > accessibility >
 * text-to-speech output (on older android versions it is under settings >
 * languages). You may have to go to the Google Play Store and install/enable
 * "Google Text-to-speech".
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class TextToSpeechActivity extends Activity {

	private TextToSpeech tts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.US);

					tts.speak("hi there.", TextToSpeech.QUEUE_FLUSH, null, null);
					tts.speak("how are you?", TextToSpeech.QUEUE_ADD, null, null);

				} else {
					Log.e("TextToSpeechActivity",
							"No speech engine available.\nDid you install 'Google Text-to-speech'?");
					Toast.makeText(TextToSpeechActivity.this,
							"No speech engine available.\nDid you install 'Google Text-to-speech'?", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	@Override
	public void onPause() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
			tts = null;
		}
		super.onPause();
	}
}