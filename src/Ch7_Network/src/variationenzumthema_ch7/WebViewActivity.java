package variationenzumthema_ch7;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import variationenzumthema.ch7.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * WebViewActivity
 *
 * This activity implements a simple web browser using the WebView widget.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class WebViewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);

		final EditText et = (EditText) findViewById(R.id.editText);

		final WebView webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient());

		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String sUri = et.getText().toString();
				if (!sUri.startsWith("http://")) {
					sUri = "http://" + sUri;
				}
				webView.loadUrl(sUri);
				webView.requestFocus();
			}
		});
	}

	/**
	 * This makes sure that you are not opening an external browser when
	 * clicking on links.
	 * 
	 * @see android-sdk/docs/resources/tutorials/views/hello-webview.html
	 */
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}