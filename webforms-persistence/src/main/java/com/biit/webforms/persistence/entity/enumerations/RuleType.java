package com.biit.webforms.persistence.entity.enumerations;

public enum RuleType {

	NORMAL(false),

	END_LOOP(true),

	END_FORM(true), ;

	private boolean destinyNull;

	RuleType(boolean destinyNull) {
		this.destinyNull = destinyNull;
	}

	public boolean isDestinyNull() {
		return destinyNull;
	}
}
