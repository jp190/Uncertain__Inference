package csp.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * A Map backed by an ArrayListSet of <key,value> pairs.
 */
public class ArrayListMap<K,V> extends AbstractMap<K,V> {
	
	protected Set<Entry<K,V>> entries =
			new ArrayListSet<Map.Entry<K,V>>();

	@Override
	public Set<Entry<K,V>> entrySet() {
		return this.entries;
	}
	
	@Override
	public V put(K key, V value) {
		for (Entry<K,V> entry : this.entries) {
			if (entry.getKey().equals(key)) {
				// Change entry value (requires mutable Entry)
				entry.setValue(value);
				return value;
			}
		}
		// Not found
		Entry<K,V> entry = new AbstractMap.SimpleEntry(key, value);
		this.entries.add(entry);
		return value;
	}

}
