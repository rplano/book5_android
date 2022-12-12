package algorithms.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * MultiHashMap
 * 
 * One key can have several values.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class MultiHashMap<K, V> {

	private HashMap<K, List<V>> map;

	public MultiHashMap() {
		map = new HashMap<K, List<V>>();
	}

	public V put(K key, V value) {
		List<V> values;
		if (map.containsKey(key)) {
			values = map.get(key);
		} else {
			values = new ArrayList<V>();
		}
		values.add(value);
		map.put(key, values);
		return value;
	}

	public void remove(K key, V value) {
		if (map.containsKey(key)) {
			List<V> values = map.get(key);
			values.remove(value);
		}
	}

	public List<V> get(K key) {
		return map.get(key);
	}

	public static void main(String[] args) throws Exception {
		MultiHashMap<String, String> map = new MultiHashMap<String, String>();
		map.put("trip", "Reise");
		map.put("trip", "Trip");
		map.put("trip", "Fahrt");
		map.put("trip", "Ausflug");
		Collection c = map.get("trip");
		System.out.println(c);
		map.remove("trip", "Ausflug");
		c = map.get("trip");
		System.out.println(c);
	}
}
