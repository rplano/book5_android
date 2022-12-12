package algorithms.container;

import java.util.HashMap;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * BidirectionalHashMap
 * 
 * A map that can be used in both directions.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class BidirectionalHashMap<K, V> {

	private HashMap<K, V> forwardMap;
	private HashMap<V, K> reverseMap;

	public BidirectionalHashMap() {
		forwardMap = new HashMap<K, V>();
		reverseMap = new HashMap<V, K>();
	}

	public V put(K key, V value) {
		forwardMap.put(key, value);
		reverseMap.put(value, key);
		return value;
	}

	public V remove(K key) {
		V value = forwardMap.get(key);
		if (value != null) {
			forwardMap.remove(key);
			reverseMap.remove(value);
		}
		return value;
	}

	public V get(K key) {
		return forwardMap.get(key);
	}

	public K getKey(V value) {
		return reverseMap.get(value);
	}

	public static void main(String[] args) throws Exception {
		BidirectionalHashMap<String, String> map = new BidirectionalHashMap<String, String>();
		map.put("dog", "hund");
		map.put("cat", "katze");
		map.put("fish", "fisch");
		map.remove("fish");
		System.out.println(map.get("dog"));
		System.out.println(map.getKey("hund"));
	}
}
