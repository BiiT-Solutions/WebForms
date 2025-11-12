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
