package algorithms.container;

import java.util.HashMap;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * HashBag
 * 
 * Bag is a set that counts its elements.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HashBag<K> {

	private HashMap<K, Integer> map;

	public HashBag() {
		map = new HashMap<K, Integer>();
	}

	public void add(K key) {
		add(key, 1);
	}

	public void add(K key, int i) {
		if (map.containsKey(key)) {
			i += map.get(key);
		}
		map.put(key, i);
	}

	public int getCount(K key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return -1;
	}

	public void remove(K key, int i) throws Exception {
		if (map.containsKey(key)) {
			int count = map.get(key) - i;
			if (count < 0) {
				count = 0;
			}
			map.put(key, count);
		} else {
			throw new Exception("bag does not contain element " + key);
		}
	}

	public static void main(String[] args) throws Exception {
		HashBag<String> b = new HashBag<String>();
		b.add("a", 8);
		b.remove("a", 2);
		System.out.println(b.getCount("a"));
		b.remove("a", 7);
		System.out.println(b.getCount("a"));
		System.out.println(b.getCount("b"));
		b.remove("b", 7);
	}
}
