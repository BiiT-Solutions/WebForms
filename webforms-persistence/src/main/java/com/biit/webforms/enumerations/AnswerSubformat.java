package com.biit.webforms.enumerations;

import java.util.regex.Pattern;

import com.biit.webforms.configuration.WebformsConfigurationReader;

public enum AnswerSubformat {

	// Text subtypes
	TEXT("", WebformsConfigurationReader.getInstance().getRegexText()),

	EMAIL("something@domain.ext", WebformsConfigurationReader.getInstance().getRegexEmail()),

	PHONE("(+NN) NNN NNN NNN", WebformsConfigurationReader.getInstance().getRegexPhone()),

	IBAN("LLNN [L/N](1-34)", WebformsConfigurationReader.getInstance().getRegexIban()),

	BSN("#9", WebformsConfigurationReader.getInstance().getRegexBsn()),

	//Number subtypes
	NUMBER("[0-9]+", WebformsConfigurationReader.getInstance().getRegexNumber()),

	FLOAT("[0-9]+\\.[0-9]*", WebformsConfigurationReader.getInstance().getRegexFloat()),
	
	AMOUNT("[0-9]+\\.[0-9]*", WebformsConfigurationReader.getInstance().getRegexAmount()),

	// Date subtypes
	DATE("dd/mm/yyyy", WebformsConfigurationReader.getInstance().getRegexDate()),

	DATE_PAST("31/12/2000", WebformsConfigurationReader.getInstance().getRegexDate()),

	DATE_FUTURE("31/12/2000", WebformsConfigurationReader.getInstance().getRegexDate()),

	DATE_PERIOD("10D", WebformsConfigurationReader.getInstance().getRegexDatePeriod()),
	
	DATE_BIRTHDAY("31/12/2000", WebformsConfigurationReader.getInstance().getRegexDateBirthday()),

	// Postal code
	POSTAL_CODE("0000AA", WebformsConfigurationReader.getInstance().getRegexPostalCode())

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

	public AnswerFormat getAnswerFormat() {
		for(AnswerFormat answerFormat: AnswerFormat.values()){
			if(answerFormat.isSubformat(this)){
				return answerFormat;
			}
		}
		return null;
	}
}
