package com.biit.webforms.persistence.entity.enumerations;

import java.util.regex.Pattern;

import com.biit.webforms.configuration.WebformsConfigurationReader;

public enum AnswerSubformat {

	// Text subtypes
	TEXT("", WebformsConfigurationReader.getInstance().getRegexText()),

	EMAIL("", WebformsConfigurationReader.getInstance().getRegexEmail()),

	PHONE("(+NN) NNN NNN NNN", WebformsConfigurationReader.getInstance().getRegexPhone()),

	IBAN("", WebformsConfigurationReader.getInstance().getRegexIban()),

	BSN("#10", WebformsConfigurationReader.getInstance().getRegexBsn()),

	// Number subtypes
	// AMMOUNT("#9", ""),

	NUMBER("", ""),

	FLOAT("", ""),

	// Date subtypes
	DATE("dd-mm-yyyy", WebformsConfigurationReader.getInstance().getRegexDate()),

	DATE_PAST("dd-mm-yyyy", WebformsConfigurationReader.getInstance().getRegexDate()),

	DATE_FUTURE("dd-mm-yyyy", WebformsConfigurationReader.getInstance().getRegexDate()),

	// BIRTHDATE("dd-mm-yyyy",""),

	// Postal code
	POSTAL_CODE("nnnnaa", WebformsConfigurationReader.getInstance().getRegexPostalCode())

	;

	private final String hint;
	private final Pattern regex;

	AnswerSubformat(String hint, String regexPattern) {
		this.hint = hint;
		this.regex = Pattern.compile(regexPattern);
	}

	public String getHint() {
		return hint;
	}

	public Pattern getRegex() {
		return regex;
	}
}
