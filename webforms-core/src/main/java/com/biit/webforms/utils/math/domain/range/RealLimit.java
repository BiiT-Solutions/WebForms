package com.biit.webforms.utils.math.domain.range;

import com.biit.webforms.utils.math.domain.Closure;

/**
 * Limits for real number ranges.
 *
 * @param <T>
 */
public class RealLimit<T extends Comparable<T>> implements Comparable<RealLimit<T>> {

	private final T limit;
	private final Closure closure;

	protected RealLimit() {
		limit = null;
		closure = null;
	}
	
	public RealLimit(T limit, Closure closure) {
		this.limit = limit;
		this.closure = closure;
	}

	public T getLimit() {
		return limit;
	}

	public Closure getClosure() {
		return closure;
	}

	public int compareTo(RealLimit<T> arg0) {
		return getLimit().compareTo(arg0.getLimit());
	}

	public boolean isSingleElement() {
		return closure == Closure.SINGLE_VALUE;
	}

	public RealLimit<T> generateCopy(){
		return new RealLimit<T>(getLimit(), getClosure());
	}
	
	@Override
	public String toString(){
		return limit+" "+closure;
	}

	public RealLimit<T> inverse(){
		return new RealLimit<T>(getLimit(), getClosure().inverse());
	}
	
}