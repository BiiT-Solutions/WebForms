package com.biit.webforms.enumerations;

public enum DatePeriodUnit {

	YEAR("Y"),

	MONTH("M"),

	DAY("D");

	// Used as units in Orbeon.
	private String abbreviature;

	DatePeriodUnit(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public String getAbbreviature() {
		return abbreviature;
	}

}
