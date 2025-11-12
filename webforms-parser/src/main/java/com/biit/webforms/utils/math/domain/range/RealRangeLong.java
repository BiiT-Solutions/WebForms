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

	public static RealRangeLong fullRange() {
		RealRangeLong realRange = new RealRangeLong();
		return new RealRangeLong(new RealLimitPair<>(realRange.negativeInfinity(), realRange.positiveInfinity()));
	}

	public static RealRangeLong negativeRange() {
		RealRangeLong realRange = new RealRangeLong();
		return new RealRangeLong(
				new RealLimitPair<>(realRange.negativeInfinity(), new RealLimit<Long>(0L, Closure.INCLUSIVE)));
	}

	public static RealRangeLong positiveRange() {
		RealRangeLong realRange = new RealRangeLong();
		return new RealRangeLong(
				new RealLimitPair<>(new RealLimit<Long>(0L, Closure.INCLUSIVE), realRange.positiveInfinity()));
	}
}
