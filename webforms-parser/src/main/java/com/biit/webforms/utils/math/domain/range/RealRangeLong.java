package com.biit.webforms.utils.math.domain.range;

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

/**
 * Concrete type for a Real range specialized for Long elements (dates)
 *
 */
public class RealRangeLong extends RealRange<Long> {

	public RealRangeLong() {
		super();
	}

	public RealRangeLong(RealLimitPair<Long> limit) {
		super(limit);
	}

	public RealRangeLong(List<RealLimitPair<Long>> limits) {
		super(limits);
	}

	public RealRangeLong(RealLimitPair<Long> first, RealLimitPair<Long> second) {
		super(first, second);
	}

	public RealRangeLong(Closure leftClosure, Long left, Long right, Closure rightClosure)
			throws LimitInsertionException {
		super(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Long> createNewRealRange(Closure leftClosure, Long left, Long right, Closure rightClosure)
			throws LimitInsertionException {
		return new RealRangeLong(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Long> createNewRealRange(RealLimitPair<Long> limit) {
		return new RealRangeLong(limit);
	}

	@Override
	protected RealRange<Long> createNewRealRange(List<RealLimitPair<Long>> limits) {
		return new RealRangeLong(limits);
	}

	@Override
	protected Long typeNegativeInfinity() {
		return Long.MIN_VALUE;
	}

	@Override
	protected Long typePositiveInfinity() {
		return Long.MAX_VALUE;
	}

	@Override
	protected boolean isDiscrete() {
		return true;
	}

	@Override
	protected Long getNextDiscreteValue(Long value) {
		return value + 1;
	}

	@Override
	public Long generateRandomValue(RealLimitPair<Long> range) {
		if (range.getLeft().getLimit().equals(range.getRight().getLimit())) {
			return range.getLeft().getLimit();
		}
		long randomNum = (random.nextInt(Integer.MAX_VALUE) % (range.getRight().getLimit() - range.getLeft().getLimit())
				+ range.getLeft().getLimit());
		return randomNum;
	}

	public static RealRangeLong positiveRange() {
		RealRangeLong realRange = new RealRangeLong();
		return new RealRangeLong(new RealLimitPair<>(realRange.negativeInfinity(), realRange.positiveInfinity()));
	}

	public static RealRangeLong negativeRange() {
		RealRangeLong realRange = new RealRangeLong();
		return new RealRangeLong(
				new RealLimitPair<>(realRange.negativeInfinity(), new RealLimit<Long>(0L, Closure.INCLUSIVE)));
	}

	public static RealRangeLong fullRange() {
		RealRangeLong realRange = new RealRangeLong();
		return new RealRangeLong(
				new RealLimitPair<>(new RealLimit<Long>(0L, Closure.INCLUSIVE), realRange.positiveInfinity()));
	}
}
