package com.biit.webforms.language;

public enum LanguageCodes {

	;

	private String value;

	private LanguageCodes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
