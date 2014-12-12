package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

/**
 * This class defines a range of real numbers. Basic mathematic operations are
 * supported.
 * 
 */
public class RealRange<T extends Comparable<T>> {

	private final List<RealLimitPair<T>> limits;

	public RealRange() {
		this.limits = new ArrayList<>();
	}

	@SafeVarargs
	public RealRange(RealLimitPair<T>... limits) {
		this.limits = Arrays.asList(limits);
	}

	public RealRange(List<RealLimitPair<T>> limits) {
		this.limits = new ArrayList<RealLimitPair<T>>(limits);
	}

//	public RealRange(Closure leftClosure, Float left, Float right,
//			Closure rightClosure) throws LimitInsertionException {
//		this.limits = new ArrayList<>();
//		setValue(leftClosure, left, right, rightClosure);
//	}

//	private void addLimit(RealLimitPair<T> limit) {
//		limits.add(limit);
//	}
//
//	private void setEmpty() {
//		limits.clear();
//	}

//	private void setValue(Float value) {
//		// try {
//		// addLimit(new RealLimit(value, Closure.SINGLE_VALUE));
//		// } catch (LimitInsertionException e) {
//		// WebformsLogger.errorMessage(this.getClass().getName(), e);
//		// }
//	}

//	private void setValue(Closure leftClosure, Float left, Float right,
//			Closure rightClosure) throws LimitInsertionException {
//		if (leftClosure == null || left == null || right == null
//				|| rightClosure == null) {
//			throw new NullPointerException(
//					"Real range can't use null as a limit value");
//		}
//		addLimit(new RealLimitPair(new RealLimitFloat(left, leftClosure),
//				new RealLimitFloat(right, rightClosure)));
//	}

//	public static RealRange<T> empty() {
//		RealRange range = new RealRange();
//		range.setEmpty();
//		return range;
//	}

//	public static RealRange value(Float value) {
//		RealRange range = new RealRange();
//		range.setValue(value);
//		return range;
//	}

//	public static RealRange eq(Float value) {
//		try {
//			return new RealRange(Closure.INCLUSIVE, value, value,
//					Closure.INCLUSIVE);
//		} catch (LimitInsertionException e) {
//			// Impossible
//			WebformsLogger.errorMessage(RealRange.class.getName(), e);
//		}
//		return null;
//	}

//	public static RealRange ne(Float value) {
//		return lt(value).union(gt(value));
//	}
//
//	public static RealRange lt(Float value) {
//		if (value == null) {
//			throw new NullPointerException(
//					"Real range can't use null as a limit value");
//		}
//		return new RealRange(RealLimitPair.lt(value));
//	}

//	public static RealRange le(Float value) {
//		if (value == null) {
//			throw new NullPointerException(
//					"Real range can't use null as a limit value");
//		}
//		return new RealRange(RealLimitPair.le(value));
//	}
//
//	public static RealRange gt(Float value) {
//		if (value == null) {
//			throw new NullPointerException(
//					"Real range can't use null as a limit value");
//		}
//		return new RealRange(RealLimitPair.gt(value));
//	}
//
//	public static RealRange ge(Float value) {
//		if (value == null) {
//			throw new NullPointerException(
//					"Real range can't use null as a limit value");
//		}
//		return new RealRange(RealLimitPair.ge(value));
//	}

	public RealRange<T> union(RealRange<T> range) {
		if (range.isEmpty()) {
			return new RealRange<T>(limits);
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

		return new RealRange<T>(unionPairs);
	}

	public RealRange<T> intersection(RealRange<T> range) {
		if (range.isEmpty()) {
			return new RealRange<T>(limits);
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

		return new RealRange<T>(intersectionPairs);
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

//	public static RealRange realRange() {
//		RealRange realRange = new RealRange();
//		realRange.setRealRange();
//		return realRange;
//	}

	public RealRange<T> inverse(RealLimitPair<T> completeDomain) {
		if (isEmpty()) {
			return new RealRange<T>(completeDomain);
		} else {
			List<RealLimitPair<T>> inverseRanges = new ArrayList<RealLimitPair<T>>();

			if (completeDomain.getLeft().compareTo(limits.get(0).getLeft()) < 0) {
				inverseRanges.add(new RealLimitPair<T>(completeDomain.getLeft(),
						limits.get(0).getLeft().inverse()));
			}

			for (int i = 1; i < limits.size(); i++) {
				inverseRanges.add(new RealLimitPair<T>(limits.get(i - 1)
						.getRight().inverse(), limits.get(i).getLeft()
						.inverse()));
			}

			int maxRange = limits.size() - 1;
			if (completeDomain.getRight().compareTo(
					limits.get(maxRange).getLeft()) > 0) {
				inverseRanges.add(new RealLimitPair<T>(limits.get(maxRange)
						.getRight().inverse(), completeDomain.getRight()));
			}

			return new RealRange<T>(inverseRanges);
		}
	}

	public boolean isComplete(RealLimitPair<T> completeDomain) {
		if (isEmpty() || limits.size() > 1) {
			return false;
		}else{
			if(limits.get(0).getLeft().compareTo(completeDomain.getLeft())==0 && limits.get(0).getRight().compareTo(completeDomain.getRight())==0){
				return true;
			}
			return false;
		}
	}
}
