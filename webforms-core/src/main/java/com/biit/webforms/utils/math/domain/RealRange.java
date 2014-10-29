package com.biit.webforms.utils.math.domain;

import com.biit.webforms.utils.math.domain.exceptions.DisjoiedRangesCantBeJoined;

/**
 * This class defines a range of real numbers. Basic mathematic operations are
 * supported.
 * 
 */
public class RealRange implements Comparable<RealRange>{

	private RangeClosure leftClosure;
	private Float left;
	private Float right;
	private RangeClosure rightClosure;

	public RealRange() {
		setValue(RangeClosure.EXCLUSIVE, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, RangeClosure.EXCLUSIVE);
	}

	public RealRange(RangeClosure leftClosure, Float left, Float right, RangeClosure rightClosure) {
		setValue(leftClosure, left, right, rightClosure);
	}

	private void setEmpty() {
		setValue(RangeClosure.EXCLUSIVE, 0.0f, 0.0f, RangeClosure.EXCLUSIVE);
	}

	private void setValue(Float value) {
		setValue(RangeClosure.INCLUSIVE, value, value, RangeClosure.INCLUSIVE);
	}

	private void setValue(RangeClosure leftClosure, Float left, Float right, RangeClosure rightClosure) {
		if (leftClosure == null || left == null || right == null || rightClosure == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		this.leftClosure = leftClosure;
		this.left = left;
		this.right = right;
		this.rightClosure = rightClosure;
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
		RealRange range = new RealRange();
		range.right = value;
		return range;
	}

	public static RealRange le(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		RealRange range = new RealRange();
		range.right = value;
		range.rightClosure = RangeClosure.INCLUSIVE;
		return range;
	}

	public static RealRange gt(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		RealRange range = new RealRange();
		range.left = value;
		return range;
	}

	public static RealRange ge(Float value) {
		if (value == null) {
			throw new NullPointerException("Real range can't use null as a limit value");
		}
		RealRange range = new RealRange();
		range.left = value;
		range.leftClosure = RangeClosure.INCLUSIVE;
		return range;
	}

	private boolean isContainedFromLeft(Float value) {
		if (leftClosure == RangeClosure.INCLUSIVE) {
			return left <= value;
		} else {
			return left < value;
		}
	}

	private boolean isContainedFromRight(Float value) {
		if (rightClosure == RangeClosure.INCLUSIVE) {
			return value <= right;
		} else {
			return value < right;
		}
	}

	public boolean contains(Float value) {
		return isContainedFromLeft(value) && isContainedFromRight(value);
	}

	private boolean isContainedFromLeft(RealRange value) {
		if (left < value.left) {
			return true;
		} else {
			if (left.equals(value.left) && leftClosure==RangeClosure.INCLUSIVE) {
				return true;
			}
		}
		return false;
	}
	private boolean isContainedFromRight(RealRange value) {
		if (right > value.right) {
			return true;
		} else {
			if (right.equals(value.right) && rightClosure==RangeClosure.INCLUSIVE) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(RealRange value){
		return isContainedFromLeft(value)&& isContainedFromRight(value);
	}

	public boolean overlap(RealRange range) {
		if (contains(range.getLeft()) || contains(range.getRight()) || range.contains(getLeft())) {
			return true;
		}
		return false;
	}

	public RealRange union(RealRange range) throws DisjoiedRangesCantBeJoined {
		if (overlap(range)) {
			RealRange newRange = new RealRange();
			if (getLeft() < range.getLeft()) {
				// this < than range
				newRange.left = getLeft();
				newRange.leftClosure = leftClosure;
			} else {
				if (range.getLeft() < getLeft()) {
					// range less than this
					newRange.left = range.getLeft();
					newRange.leftClosure = range.leftClosure;
				} else {
					// equals
					newRange.left = range.getLeft();
					if (leftClosure == RangeClosure.INCLUSIVE || range.leftClosure == RangeClosure.INCLUSIVE) {
						newRange.leftClosure = RangeClosure.INCLUSIVE;
					} else {
						newRange.leftClosure = RangeClosure.EXCLUSIVE;
					}
				}
			}
			if (getRight() > range.getRight()) {
				// this < than range
				newRange.right = getRight();
				newRange.rightClosure = rightClosure;
			} else {
				if (range.getRight() > getRight()) {
					// range less than this
					newRange.right = range.getRight();
					newRange.rightClosure = range.rightClosure;
				} else {
					// equals
					newRange.right = range.getRight();
					if (rightClosure == RangeClosure.INCLUSIVE || range.rightClosure == RangeClosure.INCLUSIVE) {
						newRange.rightClosure = RangeClosure.INCLUSIVE;
					} else {
						newRange.rightClosure = RangeClosure.EXCLUSIVE;
					}
				}
			}
			return newRange;
		} else {
			throw new DisjoiedRangesCantBeJoined();
		}
	}

	public RealRange intersection(RealRange range) {
		if (overlap(range)) {
			RealRange newRange = new RealRange();
			// Left
			if (getLeft() > range.getLeft()) {
				newRange.left = getLeft();
				newRange.leftClosure = leftClosure;
			} else {
				if (range.getLeft() > getLeft()) {
					newRange.left = range.getLeft();
					newRange.leftClosure = range.leftClosure;
				} else {
					// equals
					newRange.left = range.getLeft();
					if (leftClosure == RangeClosure.EXCLUSIVE || range.leftClosure == RangeClosure.EXCLUSIVE) {
						newRange.leftClosure = RangeClosure.EXCLUSIVE;
					} else {
						newRange.leftClosure = RangeClosure.INCLUSIVE;
					}
				}
			}
			// Right
			if (getRight() < range.getRight()) {
				newRange.right = getRight();
				newRange.rightClosure = rightClosure;
			} else {
				if (range.getRight() > getRight()) {
					newRange.right = range.getRight();
					newRange.rightClosure = range.rightClosure;
				} else {
					// equals
					newRange.right = range.getRight();
					if (rightClosure == RangeClosure.EXCLUSIVE || range.rightClosure == RangeClosure.EXCLUSIVE) {
						newRange.rightClosure = RangeClosure.EXCLUSIVE;
					} else {
						newRange.rightClosure = RangeClosure.INCLUSIVE;
					}
				}
			}
			return newRange;
		} else {
			return empty();
		}
	}

	private Float getRight() {
		return right;
	}

	private Float getLeft() {
		return left;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(leftClosure==RangeClosure.INCLUSIVE){
			sb.append("[");
		}else{
			sb.append("(");
		}
		sb.append(left);
		sb.append(",");
		sb.append(right);
		if(rightClosure==RangeClosure.INCLUSIVE){
			sb.append("]");
		}else{
			sb.append(")");
		}
		return sb.toString();
	}

	@Override
	public int compareTo(RealRange o) {
		if(left>o.left){
			return -(o.compareTo(this));
		}
		
		if(left>o.right || (left.equals(o.right) && (leftClosure==RangeClosure.EXCLUSIVE || leftClosure!=o.rightClosure))){
			return 1;
		}
		
		if(right<o.left || (right.equals(o.left) && (rightClosure==RangeClosure.EXCLUSIVE || rightClosure!=o.leftClosure))){
			return -1;
		}
		return 0;
	}

	public boolean isEmpty() {
		return left.equals(right) && (leftClosure==RangeClosure.EXCLUSIVE || rightClosure==RangeClosure.EXCLUSIVE);
	}
}
