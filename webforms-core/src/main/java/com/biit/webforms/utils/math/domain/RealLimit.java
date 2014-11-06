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

	@Override
	public int compareTo(RealLimit arg0) {
		//TODO this is under observation if no problem is given on tests delete.
//		if (limit.equals(arg0.limit)) {
//			if (closure == arg0.closure && closure == Closure.INCLUSIVE) {
//				return 0;
//			} else {
//				return -1;
//			}
//		}
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

	public RealLimit inverse() {
		return new RealLimit(limit, closure.inverse());
	}
}
