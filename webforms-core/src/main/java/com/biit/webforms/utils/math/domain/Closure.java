package com.biit.webforms.utils.math.domain;

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
