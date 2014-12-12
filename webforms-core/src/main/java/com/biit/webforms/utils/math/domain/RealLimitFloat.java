package com.biit.webforms.utils.math.domain;

public class RealLimitFloat extends RealLimit<Float> {

	public RealLimitFloat(Float limit, Closure closure) {
		super(limit,closure);
	}

	@Override
	public RealLimitFloat negativeInfinity() {
		return new RealLimitFloat(Float.NEGATIVE_INFINITY, Closure.EXCLUSIVE);
	}

	@Override
	public RealLimitFloat positiveInfinity() {
		return new RealLimitFloat(Float.POSITIVE_INFINITY, Closure.EXCLUSIVE);
	}
	
	@Override
	public RealLimitFloat newInstance(Float limit, Closure closure) {
		return new RealLimitFloat(limit, closure);
	}
	
}
