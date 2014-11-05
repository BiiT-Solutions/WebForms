package com.biit.webforms.utils.math.domain;

public class RealLimit implements Comparable<RealLimit> {

	private final Float limit;
	private final Closure closure;

	public RealLimit(Float limit, Closure closure) {
		this.limit = limit;
		this.closure = closure;
	}

	public Float getLimit() {
		return limit;
	}

	public Closure getClosure() {
		return closure;
	}

	public static RealLimit negativeInfinity() {
		return new RealLimit(Float.NEGATIVE_INFINITY, Closure.EXCLUSIVE);
	}

	public static RealLimit positiveInfinity() {
		return new RealLimit(Float.POSITIVE_INFINITY, Closure.EXCLUSIVE);
	}

	/**
	 * This compareTo method will return normal -1 0 1 if the limits are not in
	 * the same number. if They are in the same number and both are inclusive
	 * then its the same limit else arg0 will always be greater than
	 */
	@Override
	public int compareTo(RealLimit arg0) {
//		System.out.println(limit +" "+arg0.limit);
//		System.out.println(limit.compareTo(arg0.limit));
		if (limit.equals(arg0.limit)) {
			if (closure == arg0.closure && closure == Closure.INCLUSIVE) {
				return 0;
			} else {
				return -1;
			}
		}
		return limit.compareTo(arg0.limit);
	}

	public boolean isSingleElement() {
		return closure == Closure.SINGLE_VALUE;
	}

	public RealLimit generateCopy(){
		return new RealLimit(limit, closure);
	}
	
	@Override
	public String toString(){
		return limit+" "+closure;
	}
}
