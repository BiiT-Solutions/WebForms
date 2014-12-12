package com.biit.webforms.utils.math.domain;

public class RealLimitPair<T extends Comparable<T>> implements Comparable<RealLimitPair<T>>{

	private final RealLimit<T> left;
	private final RealLimit<T> right;

	public RealLimitPair(RealLimit<T> left, RealLimit<T> right) {
		this.left = left;
		this.right = right;
	}

	private boolean isContainedFromLeft(RealLimit<T> value) {
		if (left.getLimit().compareTo(value.getLimit())<0) {
			return true;
		} else {
			if(left.getLimit().equals(value.getLimit()) && (left.getClosure()==Closure.INCLUSIVE ||value.getClosure()==Closure.INCLUSIVE)){
				return true;
			}
		}
		return false;
	}

	private boolean isContainedFromRight(RealLimit<T> value) {
		if (right.getLimit().compareTo(value.getLimit())>0) {
			return true;
		} else {
			if(right.getLimit().equals(value.getLimit()) && (right.getClosure()==Closure.INCLUSIVE ||value.getClosure()==Closure.INCLUSIVE)){
				return true;
			}
		}
		return false;
	}

	public boolean isContained(RealLimit<T> value) {
		return isContainedFromLeft(value) && isContainedFromRight(value);
	}

	public boolean overlap(RealLimitPair<T> pair) {
		if (isContained(pair.getLeft()) || isContained(pair.getRight()) || pair.isContained(getLeft())) {
			return true;
		}
		return false;
	}

	public RealLimitPair<T> union(RealLimitPair<T> pair) {
		if (overlap(pair)) {
			RealLimit<T> newLeft;
			RealLimit<T> newRight;
			if (getLeft().compareTo(pair.getLeft()) < 0) {
				// this < than range
				newLeft = getLeft();
			} else {
				// this > pair
				newLeft = pair.getLeft();
			}
			if (getRight().compareTo(pair.getRight()) > 0) {
				// this > than pair
				newRight = getRight();
			} else {
				// range less or equal than this
				newRight = pair.getRight();
			}
			return new RealLimitPair<T>(newLeft, newRight);
		}
		return null;
	}
	
	
	/**
	 * Check if overlapping is strict.
	 * @param pair
	 * @return
	 */
	private boolean isOverlapStrict(RealLimitPair<T> pair) {
		if(right.getLimit().equals(pair.getLeft().getLimit())){
			if(right.getClosure()!=Closure.INCLUSIVE || pair.getLeft().getClosure()!=Closure.INCLUSIVE){
				return false;
			}
		}
		return true;
	}

	public RealLimitPair<T> intersection(RealLimitPair<T> pair) {
		if (overlap(pair) && isOverlapStrict(pair)) {
			RealLimit<T> newLeft;
			RealLimit<T> newRight;
			// Left
			if (getLeft().compareTo(pair.getLeft()) < 0) {
				// this < than range
				newLeft = pair.getLeft();
			} else {
				// this >= pair
				newLeft = getLeft();
			}
			// Right
			if (getRight().compareTo(pair.getRight()) > 0) {
				// this > than pair
				newRight = pair.getRight();
			} else {
				// range less than this
				newRight = getRight();
			}
			return new RealLimitPair<T>(newLeft, newRight);
		}
		return null;
	}

	public RealLimit<T> getLeft() {
		return left;
	}

	public RealLimit<T> getRight() {
		return right;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (left.getClosure() == Closure.EXCLUSIVE) {
			sb.append("(");
		} else {
			sb.append("[");
		}
		sb.append(left.getLimit());
		sb.append(",");
		sb.append(right.getLimit());
		if (right.getClosure() == Closure.EXCLUSIVE) {
			sb.append(")");
		} else {
			sb.append("]");
		}

		return sb.toString();
	}

	@Override
	public int compareTo(RealLimitPair<T> o) {
		if((left.compareTo(o.left) == 0 ) && ((right.compareTo(o.right)==0 ))){
			return 0;
		}
		return left.compareTo(o.left);
	}
	
//	public static RealLimitPair<T> lt(Float value){
//		return newInstance(RealLimitFloat.negativeInfinity(), new RealLimitFloat(value, Closure.EXCLUSIVE));
//	}
//	
//	public static RealLimitPair<T> le(Float value){
//		return newInstance(RealLimitFloat.negativeInfinity(), new RealLimitFloat(value, Closure.INCLUSIVE));
//	}
//	
//	public static RealLimitPair<T> gt(Float value){
//		return newInstance(new RealLimitFloat(value, Closure.EXCLUSIVE), RealLimitFloat.positiveInfinity());
//	}
//	
//	public static RealLimitPair<T> ge(Float value){
//		return newInstance(new RealLimitFloat(value, Closure.INCLUSIVE), RealLimitFloat.positiveInfinity());
//	}
//	
//	public static RealLimitPair<T> realRange(){
//		return newInstance(RealLimitFloat.negativeInfinity(), RealLimitFloat.positiveInfinity());
//	}
}
