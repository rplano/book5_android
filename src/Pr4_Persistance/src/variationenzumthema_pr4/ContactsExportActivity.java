package variationenzumthema_pr4;

import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import variationenzumthema.pr4.R;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ContactsExportActivity
 *
 * This activity exports all your contacts as a semi-colon separated file.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ContactsExportActivity extends Activity {

	private TextView tv;
	private String allContacts = "";
	private File currentFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_export_activity);

		currentFile = android.os.Environment.getExternalStorageDirectory();
		// etName = (EditText) findViewById(R.id.et_name);

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				allContacts = getAllContacts();
				tv.setText(allContacts);
				openSaveFileDialog();
			}
		});
	}

	private String getAllContacts() {
		String allContacts = "";

		String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.TIMES_CONTACTED, };

		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String orderBy = ContactsContract.Contacts.DISPLAY_NAME + " ASC";

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(uri, projection, null, null, orderBy);

		while (cur.moveToNext()) {

			String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Integer hasPhone = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			Integer timesContacted = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));

			String email = "[";
			Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
			// if (ce != null && ce.moveToFirst()) {
			while (ce.moveToNext()) {
				email += ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				email += ";";
			}
			ce.close();
			email += "]";

			String structuresName = "[";
			String whereName = ContactsContract.Data.MIMETYPE + " = ? AND "
					+ ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
			String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
					id };
			Cursor cn = cr.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams,
					ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
			while (cn.moveToNext()) {
				structuresName += cn
						.getString(cn.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
				structuresName += ";"
						+ cn.getString(cn.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
				structuresName += ";"
						+ cn.getString(cn.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
				structuresName += ";"
						+ cn.getString(cn.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
				structuresName += ";";
			}
			cn.close();
			structuresName += "]";

			String address = "[";
			Cursor ca = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", new String[] { id }, null);
			// if (ca != null && ca.moveToFirst()) {
			while (ca.moveToNext()) {
				address += ca.getString(ca.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));
				address += ";";
			}
			ca.close();
			address += "]";

			String phone = "[";
			if (hasPhone > 0) {
				Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
				// if (cp != null && cp.moveToFirst()) {
				while (cp.moveToNext()) {
					phone += cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					phone += ";";
				}
				cp.close();
			}
			phone += "]";

			String titl = "{" + id;
			titl += ";" + displayName;
			titl += ";" + timesContacted;
			titl += ";" + structuresName;
			titl += ";" + phone;
			titl += ";" + email;
			titl += ";" + address;
			allContacts += titl + "}\n";
		}
		cur.close();

		return allContacts;
	}

	private void saveFile(String fileName) {
		currentFile = new File(fileName);
		try {
			// String text = et.getText().toString();
			FileWriter fw = new FileWriter(currentFile);
			fw.write(allContacts);
			fw.close();

		} catch (Exception e) {
			Log.e("EditorActivity", e.getMessage());
		}
	}

	private void openSaveFileDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Save");

		final EditText input = new EditText(this);
		input.setText(currentFile.getAbsolutePath());
		dialog.setView(input);

		dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String fileName = input.getText().toString();
				saveFile(fileName);
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}
}
