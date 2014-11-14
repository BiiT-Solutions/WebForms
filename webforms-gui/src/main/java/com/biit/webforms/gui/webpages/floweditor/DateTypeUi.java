package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.language.LanguageCodes;

public enum DateTypeUi {

	YEAR(DatePeriodUnit.YEAR, LanguageCodes.CAPTION_YEAR, "Y"),

	MONTH(DatePeriodUnit.MONTH, LanguageCodes.CAPTION_MONTH, "M"),

	DAY(DatePeriodUnit.DAY, LanguageCodes.CAPTION_DAY, "D");

	private DatePeriodUnit datePeriodUnit;
	private LanguageCodes code;
	private String representation;

	DateTypeUi(DatePeriodUnit datePeriodUnit, LanguageCodes code, String representation) {
		this.datePeriodUnit = datePeriodUnit;
		this.code = code;
		this.representation = representation;
	}

	public DatePeriodUnit getDatePeriodUnit() {
		return datePeriodUnit;
	}

	String getTranslation() {
		return code.translation();
	}

	public String getRepresentation() {
		return representation;
	}

	public static DateTypeUi get(DatePeriodUnit datePeriodUnit) {
		for (DateTypeUi value : values()) {
			if (value.getDatePeriodUnit().equals(datePeriodUnit)) {
				return value;
			}
		}
		return null;
	}
}
