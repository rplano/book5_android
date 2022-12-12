package variationenzumthema_ch4;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * InternalStorageActivity
 *
 * This activity demonstrates how to access internal storage.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class InternalStorageActivity extends Activity {
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();
		
		try {
			FileOutputStream fos = openFileOutput("test.data", MODE_PRIVATE);
			fos.write( 42 );
			fos.close();
			
			FileInputStream fis = openFileInput("test.data");
			int i = fis.read();
			et.setText(""+i);
			fis.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		et = new EditText(this);
		et.setTextSize(24);
		ll.addView(et);

		setContentView(ll);
	}
}
