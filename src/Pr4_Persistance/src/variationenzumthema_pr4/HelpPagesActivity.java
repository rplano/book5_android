package variationenzumthema_pr4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * HelpPagesActivity
 *
 * This activity creates a WebView and loads it with a local webpage. Notice,
 * that it also loads stylesheets and images from the local storage (/res/raw/).
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HelpPagesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// this.setTitle("Page: "+pageNr);
		this.setTitle("Help");
		WebView webView = new WebView(this);
		webView.setWebViewClient(new HelpPagesWebViewClient());
		String html = readFile("");
		webView.loadDataWithBaseURL("file:///android_res/raw/", html, "text/html", "UTF-8", null);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		setContentView(webView);
	}

	/**
	 * This reads a file from the /res/raw/ folder, for instance
	 * /res/raw/index0.html Uses a little reflection, otherwise straight
	 * forward.
	 * 
	 * @param pageNr
	 *            gives the number in index0.html
	 * @return a web page as HTML
	 */
	private String readFile(String pageNr) {
		Field f = null;
		try {
			f = R.raw.class.getField("index" + pageNr);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		try {
			InputStream htmlStream = getResources().openRawResource(f.getInt(null));
			BufferedReader is = new BufferedReader(new InputStreamReader(htmlStream, "UTF8"));
			String line;
			while ((line = is.readLine()) != null) {
				sb.append(line);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * This makes sure that you are not opening an external browser when
	 * clicking on links. Depending on the link types, we perform different
	 * actions.
	 * 
	 * @see android-sdk/docs/resources/tutorials/views/hello-webview.html
	 */
	private class HelpPagesWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("test-", "url: " + url);
			// if ( url.startsWith("tel:") ) {
			// startPhoneActivity();
			// } else if ( url.startsWith("cal:") ) {
			// startCalendarActivity(url);
			// } else if ( url.startsWith("ext:") ) {
			// startExternalBrowser(url);
			// } else {
			if (url.startsWith("int:")) {
				String html = readFile(url.substring(4));
				view.loadDataWithBaseURL("file:///android_res/raw/", html, "text/html", "UTF-8", null);
				view.getSettings().setBuiltInZoomControls(true);
				view.getSettings().setSupportZoom(true);
				// view.loadUrl(url);
			}
			return true;
		}

	}
}
