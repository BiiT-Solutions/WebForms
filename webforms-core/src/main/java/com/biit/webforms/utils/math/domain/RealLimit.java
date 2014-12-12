package com.biit.webforms.utils.math.domain;

public abstract class RealLimit<T extends Comparable<T>> implements Comparable<RealLimit<T>> {

	private final T limit;
	private final Closure closure;

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
		return newInstance(getLimit(), getClosure());
	}
	
	@Override
	public String toString(){
		return limit+" "+closure;
	}

	public RealLimit<T> inverse(){
		return newInstance(getLimit(), getClosure().inverse());
	}
	
	public abstract RealLimit<T> newInstance(T limit, Closure closure);
	
	public abstract RealLimit<T> negativeInfinity();
	
	public abstract RealLimit<T> positiveInfinity();
}