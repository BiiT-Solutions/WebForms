package com.biit.webforms.enumerations;

public enum RuleType {

	NORMAL(false),

	END_LOOP(false),

	END_FORM(true), ;

	private boolean destinyNull;

	RuleType(boolean destinyNull) {
		this.destinyNull = destinyNull;
	}

	public boolean isDestinyNull() {
		return destinyNull;
	}
}
