package com.biit.webforms.utils.math.domain.range;

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

public class RealRangeInteger extends RealRange<Integer>{

	public RealRangeInteger() {
		super();
	}

	public RealRangeInteger(RealLimitPair<Integer> limit) {
		super(limit);
	}

	public RealRangeInteger(List<RealLimitPair<Integer>> limits) {
		super(limits);
	}
	
	public RealRangeInteger(RealLimitPair<Integer> first, RealLimitPair<Integer> second) {
		super(first, second);
	}

	public RealRangeInteger(Closure leftClosure, Integer left, Integer right, Closure rightClosure)
			throws LimitInsertionException {
		super(leftClosure, left, right, rightClosure);
	}
	
	@Override
	protected RealRange<Integer> createNewRealRange(Closure leftClosure, Integer left, Integer right,
			Closure rightClosure) throws LimitInsertionException {
		return new RealRangeInteger(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Integer> createNewRealRange(RealLimitPair<Integer> limit) {
		return new RealRangeInteger(limit);
	}

	@Override
	protected RealRange<Integer> createNewRealRange(List<RealLimitPair<Integer>> limits) {
		return new RealRangeInteger(limits);
	}

	@Override
	protected Integer typeNegativeInfinity() {
		return Integer.MIN_VALUE;
	}

	@Override
	protected Integer typePositiveInfinity() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected boolean isDiscrete() {
		return true;
	}

	@Override
	protected Integer getNextDiscreteValue(Integer value) {
		return value+1;
	}

}
