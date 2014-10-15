package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.language.LanguageCodes;

public enum DateTypeUi {

	YEAR(LanguageCodes.CAPTION_YEAR,"Y"),

	MONTH(LanguageCodes.CAPTION_MONTH,"M"),

	DAY(LanguageCodes.CAPTION_DAY,"D");

	private LanguageCodes code;
	private String representation;

	DateTypeUi(LanguageCodes code, String representation) {
		this.code = code;
		this.representation = representation;
	}

	String getTranslation() {
		return code.translation();
	}

	public String getRepresentation() {
		return representation;
	}
}
