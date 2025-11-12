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
		return new RealRangeDouble(
				new RealLimitPair<>(new RealLimit<Double>(0D, Closure.INCLUSIVE), realRange.positiveInfinity()));
	}

	public static RealRangeDouble negativeRange() {
		RealRangeDouble realRange = new RealRangeDouble();
		return new RealRangeDouble(
				new RealLimitPair<>(realRange.negativeInfinity(), new RealLimit<Double>(0D, Closure.INCLUSIVE)));
	}

	public static RealRangeDouble fullRange() {
		RealRangeDouble realRange = new RealRangeDouble();
		return new RealRangeDouble(new RealLimitPair<>(realRange.negativeInfinity(), realRange.positiveInfinity()));
	}

}
