package variationenzumthema_ch4;

import static android.provider.BaseColumns._ID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * DatabaseActivity
 *
 * This activity demonstrates how to access an SQLite database.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class DatabaseActivity extends Activity {

	private static final String DATABASE_NAME = "friends.db";
	private static final int DATABASE_VERSION = 2;

	private FriendsTable friends;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		friends = new FriendsTable(this, DATABASE_NAME, null, DATABASE_VERSION);
		try {
			addFriend("Merkel", "Angelika", "merkel@bundeskanzler.de");
			addFriend("Obama", "Barak", "president@whitehouse.gov");
			Cursor cursor = getFriends();
			showFriends(cursor);
		} finally {
			friends.close();
		}
	}

	private void addFriend(String lastName, String firstName, String email) {
		SQLiteDatabase db = friends.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FriendsTable.LAST_NAME, lastName);
		values.put(FriendsTable.FIRST_NAME, firstName);
		values.put(FriendsTable.EMAIL, email);
		db.insert(FriendsTable.TABLE_NAME, null, values);
	}

	private void deleteFriend(int rowID) {
		SQLiteDatabase db = friends.getWritableDatabase();
		String where = _ID + "=" + rowID;
		db.delete(FriendsTable.TABLE_NAME, where, null);
	}

	private void updateFriend(int rowID, String lastName, String firstName, String email) {
		SQLiteDatabase db = friends.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FriendsTable.LAST_NAME, lastName);
		values.put(FriendsTable.FIRST_NAME, firstName);
		values.put(FriendsTable.EMAIL, email);
		String where = _ID + "=" + rowID;
		db.update(FriendsTable.TABLE_NAME, values, where, null);
	}

	private Cursor getFriends() {
		SQLiteDatabase db = friends.getReadableDatabase();
		String[] FROM = { _ID, FriendsTable.LAST_NAME, FriendsTable.FIRST_NAME, FriendsTable.EMAIL, };
		Cursor cursor = db.query(FriendsTable.TABLE_NAME, FROM, null, null, null, null,
				FriendsTable.LAST_NAME + " DESC");
		startManagingCursor(cursor);
		return cursor;
	}

	private long getNumberOfFriendsWithRawQuery() {
		SQLiteDatabase db = friends.getReadableDatabase();
		String[] FROM = { _ID, FriendsTable.LAST_NAME, FriendsTable.FIRST_NAME, FriendsTable.EMAIL, };
		Cursor cursor = db.rawQuery(String.format("select count(*) from %s", FriendsTable.TABLE_NAME), null);
		cursor.moveToFirst();
		long count = cursor.getLong(0);
		cursor.close();
		return count;
	}

	private void showFriends(Cursor cursor) {
		StringBuffer sb = new StringBuffer();
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			sb.append(id).append(": ");
			sb.append(cursor.getString(1)).append(", ");
			sb.append(cursor.getString(2)).append(", ");
			sb.append(cursor.getString(3)).append("\n");
		}
		tv.setText(sb.toString());
	}

	private void createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		// <ScrollView
		// android:id="@+id/scrollViewId"
		// android:layout_width="wrap_content"
		// android:layout_height="wrap_content" >

		tv = new TextView(this);
		tv.setTextSize(20);
		ll.addView(tv);

		setContentView(ll);
	}

	private class FriendsTable extends SQLiteOpenHelper {

		public static final String TABLE_NAME = "friends";
		public static final String LAST_NAME = "lastName";
		public static final String FIRST_NAME = "firstName";
		public static final String EMAIL = "email";

		public FriendsTable(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LAST_NAME
					+ " TEXT NOT NULL," + FIRST_NAME + " TEXT NOT NULL," + EMAIL + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}
