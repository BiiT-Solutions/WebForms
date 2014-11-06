package com.biit.webforms.utils.math.domain;

public class RealLimitPair implements Comparable<RealLimitPair>{

	private final RealLimit left;
	private final RealLimit right;

	public RealLimitPair(RealLimit left, RealLimit right) {
		this.left = left;
		this.right = right;
	}

	private boolean isContainedFromLeft(RealLimit value) {
		if (left.getLimit() < value.getLimit()) {
			return true;
		} else {
			if(left.getLimit().equals(value.getLimit()) && (left.getClosure()==Closure.INCLUSIVE ||value.getClosure()==Closure.INCLUSIVE)){
				return true;
			}
		}
		return false;
	}

	private boolean isContainedFromRight(RealLimit value) {
		//TODO remove
//		System.out.println(right +" isContainedFromRight "+value+" "+(right.getLimit() > value.getLimit())+" "+(right.compareTo(value) >= 0));
		if (right.getLimit() > value.getLimit()) {
			return true;
		} else {
			if(right.getLimit().equals(value.getLimit()) && (right.getClosure()==Closure.INCLUSIVE ||value.getClosure()==Closure.INCLUSIVE)){
				return true;
			}
		}
		return false;
	}

	public boolean isContained(RealLimit value) {
		return isContainedFromLeft(value) && isContainedFromRight(value);
	}

	public boolean overlap(RealLimitPair pair) {
		//TODO
//		System.out.println(this+" Overlap "+pair+" "+isContained(pair.getLeft()) +" "+ isContained(pair.getRight()) +" "+ pair.isContained(getLeft()));
		if (isContained(pair.getLeft()) || isContained(pair.getRight()) || pair.isContained(getLeft())) {
			return true;
		}
		return false;
	}

	public RealLimitPair union(RealLimitPair pair) {
		if (overlap(pair)) {
			RealLimit newLeft;
			RealLimit newRight;
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
			return new RealLimitPair(newLeft, newRight);
		}
		return null;
	}
	
	/**
	 * Check if overlapping is strict.
	 * @param pair
	 * @return
	 */
	private boolean isOverlapStrict(RealLimitPair pair) {
		if(right.getLimit().equals(pair.getLeft().getLimit())){
			if(right.getClosure()!=Closure.INCLUSIVE || pair.getLeft().getClosure()!=Closure.INCLUSIVE){
				return false;
			}
		}
		return true;
	}

	public RealLimitPair intersection(RealLimitPair pair) {
		if (overlap(pair) && isOverlapStrict(pair)) {
			RealLimit newLeft;
			RealLimit newRight;
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
			return new RealLimitPair(newLeft, newRight);
		}
		return null;
	}

	public RealLimit getLeft() {
		return left;
	}

	public RealLimit getRight() {
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
	public int compareTo(RealLimitPair o) {
		if((left.compareTo(o.left) == 0 ) && ((right.compareTo(o.right)==0 ))){
			return 0;
		}
		return left.compareTo(o.left);
	}
	
	public static RealLimitPair lt(Float value){
		return new RealLimitPair(RealLimit.negativeInfinity(), new RealLimit(value, Closure.EXCLUSIVE));
	}
	
	public static RealLimitPair le(Float value){
		return new RealLimitPair(RealLimit.negativeInfinity(), new RealLimit(value, Closure.INCLUSIVE));
	}
	
	public static RealLimitPair gt(Float value){
		return new RealLimitPair(new RealLimit(value, Closure.EXCLUSIVE), RealLimit.positiveInfinity());
	}
	
	public static RealLimitPair ge(Float value){
		return new RealLimitPair(new RealLimit(value, Closure.INCLUSIVE), RealLimit.positiveInfinity());
	}
	
	public static RealLimitPair realRange(){
		return new RealLimitPair(RealLimit.negativeInfinity(), RealLimit.positiveInfinity());
	}
}
