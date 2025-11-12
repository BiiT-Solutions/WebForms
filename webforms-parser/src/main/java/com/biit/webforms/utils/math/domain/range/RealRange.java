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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.math.domain.Closure;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

/**
 * This class defines a range of real numbers with the union of several
 * {@link RealLimitPair} that define ranges. Basic mathematic operations are
 * supported.
 *
 */
public abstract class RealRange<T extends Comparable<T>> {

	protected static final Random random = new Random();
	private final List<RealLimitPair<T>> limits;

	public RealRange() {
		this.limits = new ArrayList<>();
	}

	public RealRange(RealLimitPair<T> limit) {
		this.limits = new ArrayList<>();
		addLimit(limit);
	}

	public RealRange(List<RealLimitPair<T>> limits) {
		this.limits = new ArrayList<RealLimitPair<T>>(limits);
	}

	public RealRange(Closure leftClosure, T left, T right, Closure rightClosure) throws LimitInsertionException {
		this.limits = new ArrayList<>();
		setValue(leftClosure, left, right, rightClosure);
	}

	public RealRange(RealLimitPair<T> first, RealLimitPair<T> second) {
		this.limits = new ArrayList<>();
		addLimit(first);
		addLimit(second);
	}

	private void addLimit(RealLimitPair<T> limit) {
		limits.add(limit);
	}

	private void setValue(Closure leftClosure, T left, T right, Closure rightClosure) throws LimitInsertionException {
		if (leftClosure == null || left == null || right == null || rightClosure == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		addLimit(new RealLimitPair<T>(new RealLimit<T>(left, leftClosure), new RealLimit<T>(right, rightClosure)));
	}
	
	public RealRange<T> empty(T value) {
		return null;
	}

	public RealRange<T> eq(T value) {
		try {
			return createNewRealRange(Closure.INCLUSIVE, value, value, Closure.INCLUSIVE);
		} catch (LimitInsertionException e) {
			// Impossible
			WebformsLogger.errorMessage(RealRange.class.getName(), e);
		}
		return null;
	}

	public RealRange<T> ne(T value) {
		return lt(value).union(gt(value));
	}

	public RealRange<T> lt(T value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return createNewRealRange(new RealLimitPair<T>(negativeInfinity(), new RealLimit<T>(value, Closure.EXCLUSIVE)));
	}

	public RealRange<T> le(T value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return createNewRealRange(new RealLimitPair<T>(negativeInfinity(), new RealLimit<T>(value, Closure.INCLUSIVE)));
	}

	public RealRange<T> gt(T value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return createNewRealRange(new RealLimitPair<T>(new RealLimit<T>(value, Closure.EXCLUSIVE), positiveInfinity()));
	}

	public RealRange<T> ge(T value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return createNewRealRange(new RealLimitPair<T>(new RealLimit<T>(value, Closure.INCLUSIVE), positiveInfinity()));
	}

	public RealRange<T> union(RealRange<T> range) {
		if (range.isEmpty()) {
			return createNewRealRange(limits);
		}

		List<RealLimitPair<T>> allPairs = new ArrayList<>();
		allPairs.addAll(limits);
		allPairs.addAll(range.limits);

		Collections.sort(allPairs);

		List<RealLimitPair<T>> unionPairs = new ArrayList<RealLimitPair<T>>();
		RealLimitPair<T> accum = null;
		for (int i = 0; i < allPairs.size(); i++) {
			if (accum == null) {
				accum = allPairs.get(i);
				continue;
			}

			RealLimitPair<T> nextAccum = accum.union(allPairs.get(i));
			if (isDiscrete() && nextAccum == null) {
				// Try to make an union for discretes
				if (accum.getRight().getClosure() == Closure.INCLUSIVE
						&& allPairs.get(i).getLeft().getClosure() == Closure.INCLUSIVE) {
					T nextValue = getNextDiscreteValue(accum.getRight().getLimit());
					if (nextValue.compareTo(allPairs.get(i).getLeft().getLimit()) == 0) {
						nextAccum = accum.discreteUnion(allPairs.get(i));
					}
				}
			}

			if (nextAccum == null) {
				unionPairs.add(accum);
				accum = allPairs.get(i);
			} else {
				accum = nextAccum;
			}
		}

		if (accum != null) {
			unionPairs.add(accum);
		}

		return createNewRealRange(unionPairs);
	}

	public RealRange<T> intersection(RealRange<T> range) {
		if (range.isEmpty()) {
			return createNewRealRange(limits);
		}

		List<RealLimitPair<T>> intersectionPairs = new ArrayList<RealLimitPair<T>>();
		for (RealLimitPair<T> limit : limits) {
			for (RealLimitPair<T> rangeLimit : range.limits) {
				RealLimitPair<T> intersection = limit.intersection(rangeLimit);
				if (intersection != null) {
					intersectionPairs.add(intersection);
				}
			}
		}
		return createNewRealRange(intersectionPairs);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Iterator<RealLimitPair<T>> itr = limits.iterator();
		while (itr.hasNext()) {
			sb.append(itr.next().toString());
		}
		sb.append("]");
		return sb.toString();
	}

	public boolean isEmpty() {
		return limits.isEmpty();
	}

	public RealRange<T> inverse(RealLimitPair<T> completeDomain) {
		if (isEmpty()) {
			List<RealLimitPair<T>> limits = new ArrayList<>();
			limits.add(completeDomain);
			return createNewRealRange(limits);
		} else {
			List<RealLimitPair<T>> inverseRanges = new ArrayList<RealLimitPair<T>>();

			if (completeDomain.getLeft().compareTo(limits.get(0).getLeft()) < 0) {
				inverseRanges.add(new RealLimitPair<T>(completeDomain.getLeft(), limits.get(0).getLeft().inverse()));
			}

			for (int i = 1; i < limits.size(); i++) {
				inverseRanges.add(new RealLimitPair<T>(limits.get(i - 1).getRight().inverse(),
						limits.get(i).getLeft().inverse()));
			}

			int maxRange = limits.size() - 1;
			if (completeDomain.getRight().compareTo(limits.get(maxRange).getRight()) > 0) {
				inverseRanges.add(
						new RealLimitPair<T>(limits.get(maxRange).getRight().inverse(), completeDomain.getRight()));
			}

			return createNewRealRange(inverseRanges);
		}
	}

	public boolean isComplete(RealLimitPair<T> completeDomain) {
		if (isEmpty() || limits.size() > 1) {
			return false;
		} else {
			if (limits.get(0).getLeft().compareTo(completeDomain.getLeft()) == 0
					&& limits.get(0).getRight().compareTo(completeDomain.getRight()) == 0) {
				return true;
			}
			return false;
		}
	}

	public RealRange<T> generateValue(TokenTypes type, T value) {
		switch (type) {
		case EQ:
			return eq(value);
		case NE:
			return ne(value);
		case GT:
			return gt(value);
		case GE:
			return ge(value);
		case LT:
			return lt(value);
		case LE:
			return le(value);
		case EMPTY:
			return empty(value);
		default:
			// Unexpected
			throw new RuntimeException("Unexpected default action at switch");
		}
	}

	protected abstract RealRange<T> createNewRealRange(Closure leftClosure, T left, T right, Closure rightClosure)
			throws LimitInsertionException;

	protected abstract RealRange<T> createNewRealRange(RealLimitPair<T> limit);

	protected abstract RealRange<T> createNewRealRange(List<RealLimitPair<T>> limits);

	protected abstract T typeNegativeInfinity();

	protected abstract T typePositiveInfinity();

	protected abstract boolean isDiscrete();

	protected abstract T getNextDiscreteValue(T value);

	public RealLimit<T> negativeInfinity() {
		return new RealLimit<T>(typeNegativeInfinity(), Closure.EXCLUSIVE);
	}

	public RealLimit<T> positiveInfinity() {
		return new RealLimit<T>(typePositiveInfinity(), Closure.EXCLUSIVE);
	}

	public RealLimitPair<T> domain() {
		return new RealLimitPair<T>(negativeInfinity(), positiveInfinity());
	}

	public RealRange<T> realRange() {
		return createNewRealRange(domain());
	}

	public RealLimitPair<T> pairLt(T value) {
		return new RealLimitPair<T>(negativeInfinity(), new RealLimit<T>(value, Closure.EXCLUSIVE));
	}

	public RealLimitPair<T> pairLe(T value) {
		return new RealLimitPair<T>(negativeInfinity(), new RealLimit<T>(value, Closure.INCLUSIVE));
	}

	public RealLimitPair<T> pairGt(T value) {
		return new RealLimitPair<T>(new RealLimit<T>(value, Closure.EXCLUSIVE), positiveInfinity());
	}

	public RealLimitPair<T> pairGe(T value) {
		return new RealLimitPair<T>(new RealLimit<T>(value, Closure.INCLUSIVE), positiveInfinity());
	}

	public T generateRandomValue() {
		if (limits.size() == 1) {
			return generateRandomValue(limits.get(0));
		}
		int randomNum = random.nextInt(limits.size());
		return generateRandomValue(limits.get(randomNum));
	}

	public abstract T generateRandomValue(RealLimitPair<T> range);

	public boolean contains(T value) {
		for (RealLimitPair<T> limit : limits) {
			int compareLeft = value.compareTo(limit.getLeft().getLimit());
			int compareRight = value.compareTo(limit.getRight().getLimit());

			if (compareLeft > 0 && compareRight < 0) {
				return true;
			}
			if (compareLeft == 0 && (limit.getLeft().getClosure() == Closure.INCLUSIVE
					|| limit.getLeft().getClosure() == Closure.SINGLE_VALUE)) {
				return true;
			}
			if (compareRight == 0 && (limit.getRight().getClosure() == Closure.INCLUSIVE
					|| limit.getRight().getClosure() == Closure.SINGLE_VALUE)) {
				return true;
			}
		}
		return false;
	}

}
