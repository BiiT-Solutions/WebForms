package com.biit.webforms.utils.math.domain.range;

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

public class RealRangeDouble extends RealRange<Double> {

	public RealRangeDouble() {
		super();
	}

	public RealRangeDouble(RealLimitPair<Double> limit) {
		super(limit);
	}

	public RealRangeDouble(List<RealLimitPair<Double>> limits) {
		super(limits);
	}

	public RealRangeDouble(RealLimitPair<Double> first, RealLimitPair<Double> second) {
		super(first, second);
	}

	public RealRangeDouble(Closure leftClosure, Double left, Double right, Closure rightClosure)
			throws LimitInsertionException {
		super(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Double> createNewRealRange(Closure leftClosure, Double left, Double right, Closure rightClosure)
			throws LimitInsertionException {
		return new RealRangeDouble(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<Double> createNewRealRange(RealLimitPair<Double> limit) {
		return new RealRangeDouble(limit);
	}

	@Override
	protected RealRange<Double> createNewRealRange(List<RealLimitPair<Double>> limits) {
		return new RealRangeDouble(limits);
	}

	@Override
	protected Double typeNegativeInfinity() {
		return Double.NEGATIVE_INFINITY;
	}

	@Override
	protected Double typePositiveInfinity() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	protected boolean isDiscrete() {
		return false;
	}

	@Override
	protected Double getNextDiscreteValue(Double value) {
		// No discrete value
		return null;
	}

	@Override
	public Double generateRandomValue(RealLimitPair<Double> range) {
		if (range.getLeft().getLimit().equals(range.getRight().getLimit())) {
			return range.getLeft().getLimit();
		}
		double randomNum = random.nextFloat() * (range.getRight().getLimit() - range.getLeft().getLimit());
		return randomNum;
	}

	public static RealRangeDouble positiveRange() {
		RealRangeDouble realRange = new RealRangeDouble();
		return new RealRangeDouble(new RealLimitPair<>(realRange.negativeInfinity(), realRange.positiveInfinity()));
	}

	public static RealRangeDouble negativeRange() {
		RealRangeDouble realRange = new RealRangeDouble();
		return new RealRangeDouble(
				new RealLimitPair<>(realRange.negativeInfinity(), new RealLimit<Double>(0D, Closure.INCLUSIVE)));
	}

	public static RealRangeDouble fullRange() {
		RealRangeDouble realRange = new RealRangeDouble();
		return new RealRangeDouble(
				new RealLimitPair<>(new RealLimit<Double>(0D, Closure.INCLUSIVE), realRange.positiveInfinity()));
	}

}
