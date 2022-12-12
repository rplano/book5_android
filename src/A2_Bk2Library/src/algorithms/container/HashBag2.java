package algorithms.container;

import java.util.HashMap;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * HashBag2
 * 
 * Bag is a set that counts its elements.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HashBag2<K> extends HashMap<K, Integer> {

	public HashBag2() {
		super();
	}

	public void add(K key) {
		add(key, 1);
	}

	public void add(K key, int i) {
		if (this.containsKey(key)) {
			i += this.get(key);
		}
		this.put(key, i);
	}

	public int getCount(K key) {
		if (this.containsKey(key)) {
			return this.get(key);
		}
		return -1;
	}

	public void remove(K key, int i) throws Exception {
		if (this.containsKey(key)) {
			int count = this.get(key) - i;
			if (count < 0) {
				count = 0;
			}
			this.put(key, count);
		} else {
			throw new Exception("bag does not contain element " + key);
		}
	}

	public static void main(String[] args) throws Exception {
		HashBag2<String> b = new HashBag2<String>();
		b.add("a", 8);
		b.remove("a", 2);
		System.out.println(b.getCount("a"));
		b.remove("a", 7);
		System.out.println(b.getCount("a"));

		for (String word : b.keySet()) {
			System.out.println(word + ": " + b.getCount(word));
		}

		System.out.println(b.getCount("b"));
		b.remove("b", 7);
	}
}
