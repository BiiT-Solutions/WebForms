package com.biit.webforms.utils.math.domain.range;

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

public class RealRangePostalCode extends RealRange<PostalCode> {
	
	public RealRangePostalCode() {
		super();
	}
	
	public RealRangePostalCode(RealLimitPair<PostalCode> limit) {
		super(limit);
	}

	public RealRangePostalCode(List<RealLimitPair<PostalCode>> limits) {
		super(limits);
	}
	
	public RealRangePostalCode(RealLimitPair<PostalCode> first, RealLimitPair<PostalCode> second) {
		super(first, second);
	}

	public RealRangePostalCode(Closure leftClosure, PostalCode left, PostalCode right, Closure rightClosure)
			throws LimitInsertionException {
		super(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<PostalCode> createNewRealRange(Closure leftClosure, PostalCode left, PostalCode right,
			Closure rightClosure) throws LimitInsertionException {
		return new RealRangePostalCode(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<PostalCode> createNewRealRange(RealLimitPair<PostalCode> limit) {
		return new RealRangePostalCode(limit);
	}

	@Override
	protected RealRange<PostalCode> createNewRealRange(List<RealLimitPair<PostalCode>> limits) {
		return new RealRangePostalCode(limits);
	}

	@Override
	protected PostalCode typeNegativeInfinity() {
		return PostalCode.MIN_VALUE;
	}

	@Override
	protected PostalCode typePositiveInfinity() {
		return PostalCode.MAX_VALUE;
	}

	@Override
	protected boolean isDiscrete() {
		return true;
	}

	@Override
	protected PostalCode getNextDiscreteValue(PostalCode value) {
		return value.getNextPostalCode();
	}

	@Override
	public PostalCode generateRandomValue(RealLimitPair<PostalCode> range) {
		if(range.getLeft().getLimit().equals(range.getRight().getLimit())){
			return range.getLeft().getLimit();
		}
		return PostalCode.random(range.getLeft().getLimit(),range.getRight().getLimit());
	}

}
