package com.biit.webforms.utils;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
