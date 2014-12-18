package com.biit.webforms.utils.math.domain.range;

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

public class RealStringRange extends RealRange<RangedText> {

	public RealStringRange() {
		super();
	}
	
	public RealStringRange(RealLimitPair<RangedText> limit) {
		super(limit);
	}

	public RealStringRange(List<RealLimitPair<RangedText>> limits) {
		super(limits);
	}
	
	public RealStringRange(RealLimitPair<RangedText> first, RealLimitPair<RangedText> second) {
		super(first, second);
	}

	public RealStringRange(Closure leftClosure, RangedText left, RangedText right, Closure rightClosure)
			throws LimitInsertionException {
		super(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<RangedText> createNewRealRange(Closure leftClosure, RangedText left, RangedText right,
			Closure rightClosure) throws LimitInsertionException {
		return new RealStringRange(leftClosure, left, right, rightClosure);
	}

	@Override
	protected RealRange<RangedText> createNewRealRange(RealLimitPair<RangedText> limit) {
		return new RealStringRange(limit);
	}

	@Override
	protected RealRange<RangedText> createNewRealRange(List<RealLimitPair<RangedText>> limits) {
		return new RealStringRange(limits);
	}

	@Override
	protected RangedText typeNegativeInfinity() {
		return new RangedText();
	}

	@Override
	protected RangedText typePositiveInfinity() {
		return RangedText.infiniteRangedText();
	}

	@Override
	protected boolean isDiscrete() {
		//We are treating it as real. Unions can't be done with this type
		return false;
	}

	@Override
	protected RangedText getNextDiscreteValue(RangedText value) {
		// Not possible
		return null;
	}

	@Override
	public RangedText generateRandomValue(RealLimitPair<RangedText> range) {
		if(range.getLeft().getLimit().equals(range.getRight().getLimit())){
			return range.getLeft().getLimit();
		}
		//No random between 
		return null;
	}
	
	
	
}
