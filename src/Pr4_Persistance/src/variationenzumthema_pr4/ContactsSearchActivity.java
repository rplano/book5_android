package variationenzumthema_pr4;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
 * ContactsSearchActivity
 *
 * This activity searches in your contacts for names and phone numbers. It uses
 * the SQL "LIKE" operator and is a little dangerous.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ContactsSearchActivity extends Activity {

	private EditText etName;
	private EditText etTelephone;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_search_activity);

		etName = (EditText) findViewById(R.id.et_name);
		etTelephone = (EditText) findViewById(R.id.et_telephone);

		tv = (TextView) findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());

		Button btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = etName.getText().toString();
				String phoneNr = etTelephone.getText().toString();
				if (name != null && name.length() > 0) {
					tv.setText(searchNameInContacts(name));
				} else if (phoneNr != null && phoneNr.length() > 0) {
					// tv.setText("" + getAllContactIds().size());
					tv.setText(searchTelephoneInContacts(phoneNr));
				}
			}
		});
	}

	private String searchNameInContacts(String name) {
		String names = "";

		String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.TIMES_CONTACTED, };

		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ? ";
		String[] selectionArgs = new String[] { name };
		String orderBy = ContactsContract.Contacts.TIMES_CONTACTED + " DESC";

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(uri, projection, selection, selectionArgs, orderBy);

		while (cur.moveToNext()) {

			String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Integer hasPhone = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			Integer timesContacted = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));

			String email = "";
			Cursor ce = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
			// if (ce != null && ce.moveToFirst()) {
			while (ce.moveToNext()) {
				email += ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)) + ",";
			}
			ce.close();

			String phone = "";
			if (hasPhone > 0) {
				Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
				// if (cp != null && cp.moveToFirst()) {
				while (cp.moveToNext()) {
					phone += cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + ",";
				}
				cp.close();
			}

			String titl = id;
			titl += ":" + displayName;
			titl += ":" + timesContacted;
			titl += ":" + phone;
			titl += ":" + email;
			names += titl + "\n";
		}
		cur.close();

		return names;
	}

	private String searchTelephoneInContacts(String telephone) {
		String numbers = "";

		String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER, ContactsContract.Contacts.TIMES_CONTACTED, };

		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(uri, projection, null, null, null);

		while (cur.moveToNext()) {

			String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Integer hasPhone = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			Integer timesContacted = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));

			// String email = "";
			// Cursor ce =
			// cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
			// null,
			// ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new
			// String[] { id }, null);
			// //if (ce != null && ce.moveToFirst()) {
			// while (ce.moveToNext()) {
			// email +=
			// ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))+",";
			// }
			// ce.close();

			String phone = null;
			if (hasPhone > 0) {
				String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND ";
				selection += ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
				String[] selectionArgs = new String[] { id, telephone };

				Cursor cp = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection, selectionArgs,
						null);
				if (cp != null && cp.moveToFirst()) {
					// while (cp.moveToNext()) {
					phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
				cp.close();
			}

			if (phone != null && phone.length() > 0) {
				String titl = id;
				titl += ":" + displayName;
				titl += ":" + timesContacted;
				titl += ":" + phone;
				numbers += titl + "\n";
			}
		}
		cur.close();

		return numbers;
	}

	private List<Long> getAllContactIds() {
		List<Long> ids = new ArrayList<Long>();

		String[] projection = { ContactsContract.Contacts._ID, };

		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		Cursor cur = getContentResolver().query(uri, projection, null, null, null);

		while (cur.moveToNext()) {
			ids.add((long) cur.getColumnIndex(ContactsContract.Contacts._ID));
		}
		cur.close();

		return ids;
	}
}
