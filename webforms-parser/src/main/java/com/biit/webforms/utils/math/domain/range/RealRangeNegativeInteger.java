package com.biit.webforms.utils.math.domain.range;

import com.biit.webforms.utils.math.domain.Closure;

public class RealRangeNegativeInteger extends RealRangeInteger {

	@Override
	public RealLimitPair<Integer> domain() {
		return new RealLimitPair<Integer>(negativeInfinity(), new RealLimit<Integer>(0, Closure.INCLUSIVE));
	}

}
