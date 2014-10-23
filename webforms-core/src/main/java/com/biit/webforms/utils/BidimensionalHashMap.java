package com.biit.webforms.utils;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

public class BidimensionalHashMap<T, U, V> {

	private HashMap<SimpleEntry<T, U>, V> map;

	public BidimensionalHashMap() {
		map = new HashMap<>();
	}

	public void put(T key1, U key2, V value) {
		map.put(new SimpleEntry<T, U>(key1, key2), value);
	}
	
	public V get(T key1, U key2) {
		return map.get(new SimpleEntry<T, U>(key1, key2));
	}

	public boolean containsValue(T key1, U key2) {
		return map.containsValue(new SimpleEntry<T, U>(key1, key2));
	}

	public boolean containsKey(T key1, U key2) {
		return map.containsKey(new SimpleEntry<T, U>(key1, key2));
	}

}
