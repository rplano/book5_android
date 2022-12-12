package variationenzumthema_pr8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;
import variationenzumthema.pr8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * AudioBookCreatorActivity
 *
 * This activity converts books into audio files.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class AudioBookCreatorActivity extends Activity {

	private final int DELAY = 1000;
	private final String DESTINATION_FILE = "/sdcard/tts";

	private TextToSpeech tts;

	private List<String> textList;
	private int textListIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		textList = readFromResource();

		tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.US);

					setProgressListener();

					doSpeak();

				} else {
					Toast.makeText(AudioBookCreatorActivity.this,
							"No speech engine available.\nDid you install 'Google Text-to-speech'?", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	private void setProgressListener() {
		tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

			@Override
			public void onDone(String utteranceId) {
				if (textListIndex < textList.size()) {
					doSpeak();
				}
			}

			@Override
			public void onStart(String utteranceId) {
			}

			@Override
			public void onError(String utteranceId) {
			}

		});
	}

	private void doSpeak() {
		pause(DELAY);

		final String speech = textList.get(textListIndex);
		tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null, speech);
		// tts.synthesizeToFile(speech, null, new File(DESTINATION_FILE +
		// textListIndex + ".wav"), speech);
		textListIndex++;

		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(AudioBookCreatorActivity.this, speech, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
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

	private List<String> readFromResource() {
		List<String> text = new ArrayList<String>();
		StringBuilder total = new StringBuilder();
		try {
			InputStream is = getResources().openRawResource(R.raw.tom_sawyer);
			BufferedReader r = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = r.readLine()) != null) {
				if (line.length() > 0) {
					total.append(line);
				} else {
					String t = total.toString();
					if (t.length() > 0) {
						text.add(t);
					}
					total = new StringBuilder();
				}
			}
			// OBOB
			String t = total.toString();
			if (t.length() > 0) {
				text.add(t);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
