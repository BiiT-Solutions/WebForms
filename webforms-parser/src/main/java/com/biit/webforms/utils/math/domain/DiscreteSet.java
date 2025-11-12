package com.biit.webforms.utils.math.domain;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * This class defines a DiscreteDomain based on a set for an specific type of
 * objects. It allows common domain operation
 *
 * @param <T>
 */
public class DiscreteSet<T> {

	private static Random random = new Random();
	
	private Set<T> domainMembers;

	public DiscreteSet() {
		domainMembers = new HashSet<>();
	}

	@SafeVarargs
	public final void add(T... elements) {
		for (T element : elements) {
			domainMembers.add(element);
		}
	}
	
	public T getRandomElement(){
		if(domainMembers.isEmpty()){
			return null;
		}
		
		int randomNum = random.nextInt((domainMembers.size()-1) + 1);
		Iterator<T> itr = domainMembers.iterator();
		T randomElement = itr.next();
		int i = 0;
		while(itr.hasNext()){
			if(i<randomNum){
				return  randomElement;
			}
			itr.next();
		}
		return randomElement;
	}

	public boolean contains(T element) {
		return domainMembers.contains(element);
	}

	public DiscreteSet<T> union(DiscreteSet<T> domain) {
		return union(domain.getDomainMembers());
	}

	public DiscreteSet<T> union(Set<T> domain) {
		DiscreteSet<T> unionDomain = new DiscreteSet<T>();

		for (T member : domain) {
			unionDomain.add(member);
		}
		for (T member : domainMembers) {
			unionDomain.add(member);
		}

		return unionDomain;
	}

	public DiscreteSet<T> intersection(DiscreteSet<T> domain) {
		return intersection(domain.getDomainMembers());
	}

	public DiscreteSet<T> intersection(Set<T> domain) {
		DiscreteSet<T> intersectionDomain = new DiscreteSet<T>();

		for (T member : domain) {
			if (contains(member)) {
				intersectionDomain.add(member);
			}
		}

		return intersectionDomain;
	}

	public DiscreteSet<T> difference(DiscreteSet<T> set) {
		return difference(set.getDomainMembers());
	}

	public DiscreteSet<T> difference(Set<T> elements) {
		DiscreteSet<T> differenceSet = new DiscreteSet<T>();

		for (T element : elements) {
			if (!contains(element)) {
				differenceSet.add(element);
			}
		}
		for (T element : domainMembers) {
			if (!elements.contains(element)) {
				differenceSet.add(element);
			}
		}

		return differenceSet;
	}

	public Set<T> getDomainMembers() {
		return domainMembers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domainMembers == null) ? 0 : domainMembers.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		DiscreteSet<T> other = (DiscreteSet<T>) obj;
		if (domainMembers == null) {
			if (other.domainMembers != null)
				return false;
		} else if (!domainMembers.equals(other.domainMembers))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Iterator<T> itr = domainMembers.iterator();
		while (itr.hasNext()) {
			T next = itr.next();
			sb.append("{");
			sb.append(next.toString());
			sb.append("}");
			if (itr.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public boolean isEmpty() {
		return domainMembers.isEmpty();
	}

}
