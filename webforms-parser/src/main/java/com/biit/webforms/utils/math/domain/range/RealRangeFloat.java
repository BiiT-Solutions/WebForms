package com.biit.webforms.utils.math.domain.range;

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

/**
 * Specialization of real ranges with float elements.
 *
 */
public class RealRangeFloat extends RealRange<Float> {

	public RealRangeFloat() {
		super();
	}

	public RealRangeFloat(RealLimitPair<Float> limit) {
		super(limit);
	}

	public RealRangeFloat(List<RealLimitPair<Float>> limits) {
		super(limits);
	}

	public RealRangeFloat(RealLimitPair<Float> first, RealLimitPair<Float> second) {
		super(first, second);
	}

	public RealRangeFloat(Closure leftClosure, Float left, Float right, Closure rightClosure) throws LimitInsertionException {
		super(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Float> createNewRealRange(Closure leftClosure, Float left, Float right, Closure rightClosure)
			throws LimitInsertionException {
		return new RealRangeFloat(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Float> createNewRealRange(RealLimitPair<Float> limit) {
		return new RealRangeFloat(limit);
	}

	@Override
	protected RealRange<Float> createNewRealRange(List<RealLimitPair<Float>> limits) {
		return new RealRangeFloat(limits);
	}

	@Override
	protected Float typeNegativeInfinity() {
		return Float.NEGATIVE_INFINITY;
	}

	@Override
	protected Float typePositiveInfinity() {
		return Float.POSITIVE_INFINITY;
	}

	@Override
	protected boolean isDiscrete() {
		return false;
	}

	@Override
	protected Float getNextDiscreteValue(Float value) {
		// No discrete value
		return null;
	}

	@Override
	public Float generateRandomValue(RealLimitPair<Float> range) {
		if (range.getLeft().getLimit().equals(range.getRight().getLimit())) {
			return range.getLeft().getLimit();
		}
		float randomNum = random.nextFloat() * (range.getRight().getLimit() - range.getLeft().getLimit());
		return randomNum;
	}

}
