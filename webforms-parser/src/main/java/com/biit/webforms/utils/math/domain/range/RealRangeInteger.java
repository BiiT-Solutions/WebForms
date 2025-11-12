package com.biit.webforms.utils.math.domain.range;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.List;

import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

/**
 * Specialization of real ranges with integer elements.
 *
 */
public class RealRangeInteger extends RealRange<Integer> {

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
		return value + 1;
	}

	@Override
	public Integer generateRandomValue(RealLimitPair<Integer> range) {
		if(range.getLeft().getLimit().equals(range.getRight().getLimit())){
			return range.getLeft().getLimit();
		}
		int randomNum = (random.nextInt(Integer.MAX_VALUE)%(range.getRight().getLimit() - range.getLeft().getLimit())) + range.getLeft().getLimit();
		return randomNum;
	}

}
