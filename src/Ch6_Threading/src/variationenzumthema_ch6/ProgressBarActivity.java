package variationenzumthema_ch6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ProgressBarActivity
 *
 * This activity shows how to implement a progress bar using an AsyncTask.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ProgressBarActivity extends Activity {
	private ProgressDialog progressDialog;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("Downloading file...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setMax(100);
		progressDialog.show();

		new DownloadTask().execute("I am the parameter");
	}

	// Params, Progress, Result
	private class DownloadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			Log.i("DownloadTask.onPreExecute()", "starting...");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.i("DownloadTask.onProgressUpdate()", "percent=" + values[0]);
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected String doInBackground(String... params) {
			String response = params[0] + " - I am the return value.";
			for (int i = 1; i <= 10; i++) {
				try {
					Thread.sleep(1000);
					publishProgress(i * 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("DownloadTask.onPostExecute()", "done: result=" + result);
			progressDialog.dismiss();
			Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		}
	}

}
