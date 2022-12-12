package variationenzumthema_ch4;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 *
 * Book
 *
 * This class is used in the ORMActivity example.
 *
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class Book extends SugarRecord {
	@Unique
	String isbn;
	String title;
	String author;

	// Default constructor is necessary for SugarRecord
	public Book() {
	}

	public Book(String isbn, String title, String author) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
	}

	private boolean checkISBNNumber(String isbnNumber) {
		int s = 0;
		int t = 0;

		char[] arr = isbnNumber.toCharArray();
		for (int i = 0; i < 9; i++) {
			t = arr[i] - '0';
			s += t * (i + 1);
		}

		// last digit could be a 10, i.e. an 'X':
		if (arr[9] == 'X') {
			s += 10 * 10;
		} else {
			t = arr[9] - '0';
			s += t * 10;
		}

		return (s % 11 == 0);
	}
}
