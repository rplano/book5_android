package variationenzumthema_pr4;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * MusicSearchActivity
 *
 * This activity searches in your media store for a music title or an artist. It
 * uses the SQL "LIKE" operator and is a little dangerous.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MusicSearchActivity extends Activity {

	private EditText etTitle;
	private EditText etArtist;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_search_activity);

		etTitle = (EditText) findViewById(R.id.et_title);
		etArtist = (EditText) findViewById(R.id.et_artist);

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = etTitle.getText().toString();
				String artist = etArtist.getText().toString();
				if (title != null && title.length() > 0) {
					tv.setText(searchTitleFromMediaStore(title));
				} else if (artist != null && artist.length() > 0) {
					tv.setText(searchArtistFromMediaStore(artist));
				}
			}
		});
	}

	private String searchArtistFromMediaStore(String artist) {
		String artists = "";

		String[] projection = { "_id", MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.COMPOSER, };

		// Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.ARTIST + " LIKE ? ";
		String[] selectionArgs = new String[] { artist };

		Cursor cur = getContentResolver().query(uri, projection, selection, selectionArgs, null);

		while (cur.moveToNext()) {
			String titl = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
			titl += ":" + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			titl += ":" + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
			artists += titl + "\n";
		}
		cur.close();

		return artists;
	}

	private String searchTitleFromMediaStore(String title) {
		String titles = "";

		String[] projection = { "_id", MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.COMPOSER, };

		// Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.TITLE + " LIKE ? ";
		String[] selectionArgs = new String[] { title };

		Cursor cur = getContentResolver().query(uri, projection, selection, selectionArgs, null);

		while (cur.moveToNext()) {
			String titl = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
			titl += ":" + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			titl += ":" + cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
			titles += titl + "\n";
		}
		cur.close();

		return titles;
	}
}
