package com.biit.webforms.utils.math.domain;

public class RealLimitLong extends RealLimit<Long> {

	public RealLimitLong(Long limit, Closure closure) {
		super(limit,closure);
	}

	@Override
	public RealLimitLong negativeInfinity() {
		return new RealLimitLong(Long.MIN_VALUE, Closure.EXCLUSIVE);
	}

	@Override
	public RealLimitLong positiveInfinity() {
		return new RealLimitLong(Long.MAX_VALUE, Closure.EXCLUSIVE);
	}

	@Override
	public RealLimitLong newInstance(Long limit, Closure closure) {
		return new RealLimitLong(limit, closure);
	}
}
