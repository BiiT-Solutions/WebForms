package com.biit.webforms.utils.math.domain.range;

import com.biit.webforms.utils.math.domain.Closure;

public class RealRangePositiveFloat extends RealRangeFloat {

	@Override
	public RealLimitPair<Float> domain() {
		return new RealLimitPair<Float>(new RealLimit<Float>(0.0f, Closure.INCLUSIVE), positiveInfinity());
	}

}