package com.biit.webforms.utils.math.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class defines a DiscreteDomain based on a set for an specific type of
 * objects. It allows common domain operation
 * 
 * @param <T>
 */
public class DiscreteSet<T> {

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
