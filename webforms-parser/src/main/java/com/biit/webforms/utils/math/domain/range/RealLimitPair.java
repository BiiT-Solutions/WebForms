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

import com.biit.webforms.utils.math.domain.Closure;

/**
 * A pair of real limit elements generates a range of real numbers
 *
 * @param <T>
 */
public class RealLimitPair<T extends Comparable<T>> implements Comparable<RealLimitPair<T>> {

	private final RealLimit<T> left;
	private final RealLimit<T> right;

	protected RealLimitPair() {
		left = null;
		right = null;
	}

	public RealLimitPair(RealLimit<T> left, RealLimit<T> right) {
		this.left = left;
		this.right = right;
	}

	private boolean overlap(RealLimitPair<T> first, RealLimitPair<T> second, boolean strict) {
		if (first.getRight().compareTo(second.getLeft()) < 0) {
			return false;
		}
		if (first.getRight().compareTo(second.getLeft()) == 0 && first.getRight().getClosure() == Closure.EXCLUSIVE
				&& second.getLeft().getClosure() == Closure.EXCLUSIVE) {
			return false;
		}
		if (strict && first.getRight().compareTo(second.getLeft()) == 0) {
			if (first.getRight().getClosure() == Closure.INCLUSIVE && second.getLeft().getClosure() == Closure.INCLUSIVE) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public RealLimitPair<T> unionOrdered(RealLimitPair<T> first, RealLimitPair<T> second) {
		if (overlap(first, second, false)) {
			RealLimit<T> newLeft;
			RealLimit<T> newRight;

			newLeft = first.getLeft();
			if (first.getLeft().compareTo(second.getLeft()) == 0) {
				if (second.getLeft().getClosure() == Closure.INCLUSIVE) {
					newLeft = second.getLeft();
				}
			}

			if (first.getRight().compareTo(second.getRight()) <= 0) {
				// second, right is the greatest limit.
				newRight = second.getRight();
				if (first.getRight().compareTo(second.getRight()) == 0 && first.getRight().getClosure() == Closure.INCLUSIVE) {
					newRight = first.getRight();
				}
			} else {
				newRight = first.getRight();
			}

			return new RealLimitPair<T>(newLeft, newRight);
		}
		return null;
	}

	public RealLimitPair<T> intersectionOrdered(RealLimitPair<T> first, RealLimitPair<T> second) {
		if (overlap(first, second, true)) {
			RealLimit<T> newLeft;
			RealLimit<T> newRight;
			// Left
			if (first.getLeft().compareTo(second.getLeft()) > 0) {
				newLeft = first.getLeft();
			} else {
				newLeft = second.getLeft();
				if (first.getLeft().compareTo(second.getLeft()) == 0 && first.getLeft().getClosure() == Closure.EXCLUSIVE) {
					newLeft = first.getLeft();
				}
			}

			if (first.getRight().compareTo(second.getRight()) < 0) {
				newRight = first.getRight();
			} else {
				newRight = second.getRight();
				if (first.getRight().compareTo(second.getRight()) == 0 && first.getRight().getClosure() == Closure.EXCLUSIVE) {
					newRight = first.getRight();
				}
			}

			return new RealLimitPair<T>(newLeft, newRight);
		}
		return null;
	}

	public RealLimitPair<T> union(RealLimitPair<T> pair) {
		if (this.compareTo(pair) < 0) {
			return (RealLimitPair<T>) unionOrdered(this, pair);
		} else {
			return (RealLimitPair<T>) unionOrdered(pair, this);
		}
	}

	public RealLimitPair<T> intersection(RealLimitPair<T> pair) {
		if (this.compareTo(pair) < 0) {
			return (RealLimitPair<T>) intersectionOrdered(this, pair);
		} else {
			return (RealLimitPair<T>) intersectionOrdered(pair, this);
		}
	}

	public RealLimitPair<T> discreteUnion(RealLimitPair<T> pair) {
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
		if ((right.compareTo(o.left) == 0) && (left.compareTo(o.left) <= 0)) {
			return -1;
		}
		if ((left.compareTo(o.right) == 0) && (right.compareTo(o.right) >= 0)) {
			return 1;
		}
		return left.compareTo(o.left);
	}
}
