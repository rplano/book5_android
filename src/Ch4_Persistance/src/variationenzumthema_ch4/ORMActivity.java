package variationenzumthema_ch4;

import java.util.List;

import com.orm.SugarContext;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * ORMActivity
 *
 * This activity demonstrates the use of Sugar ORM.
 * 
 * You need to include the guava-19.0.jar and sugar-1.4.jar in your project.
 *
 * @see http://satyan.github.io/sugar/index.html
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class ORMActivity extends Activity {
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createUI();

		SugarContext.init(this);

		// create new book:
		Book book = new Book("1530651840", "Java", "Ralph Lano");
		book.save();

		// load entity:
		Book book2 = Book.findById(Book.class, 1);
		if (book2 != null) {
			tv.setText(book2.title + ", "+book2.author+", "+book2.isbn);
		}

		// // get all:
		// Iterator<Book> booksIt = Book.findAll(Book.class);
		// String txt = "";
		// while (booksIt.hasNext()) {
		// txt += booksIt.next().title;
		// }
		// tv.setText(txt);

		// search:
		// List<Book> books = Book.find(Book.class, "isbn = ?", "isbn456");
		// String txt = "";
		// for (Book book3 : books) {
		// txt += book3.title;
		// }
		// tv.setText(txt);

		// SQL:
		List<Book> books = Book.findWithQuery(Book.class, "SELECT * FROM Book WHERE isbn LIKE ?", "153%");
		String txt = "";
		for (Book book3 : books) {
			txt += book3.title + ", "+book3.author+", "+book3.isbn;
		}
		tv.setText(txt);

		// // update entity:
		// Book book3 = Book.findById(Book.class, 1);
		// book3.title = "updated title here"; // modify the values
		// book3.edition = "3rd edition";
		// book3.save(); // updates the previous entry with new values.

		// // delete entity:
		// Book book = Book.findById(Book.class, 1);
		// book.delete();

		// // update Entity based on Unique values
		// Book book = new Book("isbn123", "Title here", "2nd edition");
		// book.save();
		//
		// // update book with isbn123:
		// Book sameBook = new Book("isbn123", "New Title", "5th edition");
		// sameBook.update();
		//
		// tv.setText("" + (book.getId() == sameBook.getId())); // true

		// // bulk insert:
		// List<Book> books = new ArrayList<>();
		// books.add(new Book("1530651840", "Java", "Ralph Lano"));
		// books.add(new Book("1545467463", "Algorithmen", "Ralph Lano"));
		// books.add(new Book("1537765469", "Internet", "Ralph Lano"));
		// SugarRecord.saveInTx(books);

		// Book book2 = Book.findById(Book.class, 2);
		// if (book2 != null) {
		// tv.setText(book2.title);
		// }

		SugarContext.terminate();
	}

	private void createUI() {
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(0x200000ff);
		ll.setOrientation(LinearLayout.VERTICAL);

		tv = new TextView(this);
		tv.setTextSize(20);
		ll.addView(tv);

		setContentView(ll);
	}

}