package com.biit.webforms.persistence.entity.enumerations;

public enum RuleType {

	NORMAL(false,false),
	
	OTHERS(false,true),
	
	LOOP(true,false),

	END_LOOP(true,false),

	END_FORM(true,false), ;

	private boolean destinyNull;
	private boolean others;

	RuleType(boolean destinyNull, boolean others) {
		this.destinyNull = destinyNull;
		this.others = others;
	}

	public boolean isDestinyNull() {
		return destinyNull;
	}
	
	public boolean isOthers(){
		return others;
	}
}
