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
public class RealRange {

	private final List<RealLimitPair> limits;

	public RealRange() {
		this.limits = new ArrayList<>();
	}
	
	public RealRange(RealLimitPair ... limits){
		this.limits = Arrays.asList(limits);
	}

	public RealRange(List<RealLimitPair> limits) {
		this.limits = new ArrayList<RealLimitPair>(limits);
	}

	public RealRange(Closure leftClosure, Float left, Float right, Closure rightClosure) throws LimitInsertionException {
		this.limits = new ArrayList<>();
		setValue(leftClosure, left, right, rightClosure);
	}

	public RealRange(RealLimit leftLimit, RealLimit rightLimit) throws LimitInsertionException {
		this.limits = new ArrayList<>();
		addLimit(new RealLimitPair(leftLimit, rightLimit));
	}

	private void setRealRange() {
		setEmpty();
		addLimit(new RealLimitPair(RealLimit.negativeInfinity(), RealLimit.positiveInfinity()));
	}

	private void addLimit(RealLimitPair limit) {
		limits.add(limit);
	}

	private void setEmpty() {
		limits.clear();
	}

	private void setValue(Float value) {
		// try {
		// addLimit(new RealLimit(value, Closure.SINGLE_VALUE));
		// } catch (LimitInsertionException e) {
		// WebformsLogger.errorMessage(this.getClass().getName(), e);
		// }
	}

	private void setValue(Closure leftClosure, Float left, Float right, Closure rightClosure)
			throws LimitInsertionException {
		if (leftClosure == null || left == null || right == null || rightClosure == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		addLimit(new RealLimitPair(new RealLimit(left, leftClosure), new RealLimit(right, rightClosure)));
	}

	public static RealRange empty() {
		RealRange range = new RealRange();
		range.setEmpty();
		return range;
	}

	public static RealRange value(Float value) {
		RealRange range = new RealRange();
		range.setValue(value);
		return range;
	}

	public static RealRange lt(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return new RealRange(RealLimitPair.lt(value));
	}

	public static RealRange le(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return new RealRange(RealLimitPair.le(value));
	}

	public static RealRange gt(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return new RealRange(RealLimitPair.gt(value));
	}

	public static RealRange ge(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		return new RealRange(RealLimitPair.ge(value));
	}

	public RealRange union(RealRange range) {
		if(range.isEmpty()){
			return new RealRange(limits);
		}
		
		List<RealLimitPair> allPairs = new ArrayList<>();
		allPairs.addAll(limits);
		allPairs.addAll(range.limits);
		
		Collections.sort(allPairs);
		
		List<RealLimitPair> unionPairs = new ArrayList<RealLimitPair>();
		RealLimitPair accum = null;
		for(int i=0;i<allPairs.size();i++){
			if(accum==null){
				accum = allPairs.get(i);
				continue;
			}
			
			RealLimitPair nextAccum = accum.union(allPairs.get(i));
			if(nextAccum==null){
				unionPairs.add(accum);
				accum = allPairs.get(i);
			}else{
				accum = nextAccum;
			}
		}
		
		if(accum!=null){
			unionPairs.add(accum);
		}
		
		return new RealRange(unionPairs);
	}

	public RealRange intersection(RealRange range) {
		if(range.isEmpty()){
			return new RealRange(limits);
		}
			
		List<RealLimitPair> intersectionPairs = new ArrayList<RealLimitPair>();
		for(RealLimitPair limit: limits){
			for(RealLimitPair rangeLimit: range.limits){
				RealLimitPair intersection = limit.intersection(rangeLimit);
				if(intersection != null){
					intersectionPairs.add(intersection);
				}
			}
		}
		
		return new RealRange(intersectionPairs);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Iterator<RealLimitPair> itr = limits.iterator();
		while (itr.hasNext()) {
			sb.append(itr.next().toString());
		}
		sb.append("]");
		return sb.toString();
	}

	public boolean isEmpty() {
		return limits.isEmpty();
	}

	public static RealRange realRange() {
		RealRange realRange = new RealRange();
		realRange.setRealRange();
		return realRange;
	}
}
