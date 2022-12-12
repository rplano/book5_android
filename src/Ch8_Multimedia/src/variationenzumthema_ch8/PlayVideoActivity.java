package variationenzumthema_ch8;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;
import variationenzumthema.ch8.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * PlayVideoActivity
 *
 * This activity shows how to play a video using a VideoView.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class PlayVideoActivity extends Activity {

	private VideoView video;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_video_activity);

		video = (VideoView) findViewById(R.id.videoView);
		// video.setVideoPath("/sdcard/socialweb.mp4");
		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.socialweb);
		video.setVideoURI(uri);
		// video.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.icon_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_fast_rewind:
			video.seekTo(video.getCurrentPosition() - 5000);
			return true;

		case R.id.action_play:
			// video.seekTo(0);
			video.start();
			return true;

		case R.id.action_pause:
			video.pause();
			return true;

		case R.id.action_stop:
			video.stopPlayback();
			return true;

		case R.id.action_fast_forward:
			video.seekTo(video.getCurrentPosition() + 5000);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}