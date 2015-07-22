package com.biit.webforms.utils.math.domain;

/**
 * Enum definition to represent closures of ranges [ ( {}
 */
public enum Closure {

	INCLUSIVE, 
	EXCLUSIVE,
	SINGLE_VALUE,
	;

	public Closure inverse() {
		switch (this) {
		case INCLUSIVE:
			return EXCLUSIVE;
		case EXCLUSIVE:
			return INCLUSIVE;
		default:
			return null;
		}
	}
}
