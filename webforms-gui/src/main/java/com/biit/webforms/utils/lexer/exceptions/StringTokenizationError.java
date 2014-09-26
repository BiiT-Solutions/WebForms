package com.biit.webforms.utils.lexer.exceptions;

public class StringTokenizationError extends Exception {
	private static final long serialVersionUID = -7829493183545491834L;

	private String fullString;
	private String notRecognizedString;
	
	public StringTokenizationError(String fullString, String notRecognizedString) {
		super("String could not be tokenized since position: "+fullString.lastIndexOf(notRecognizedString));
		this.fullString = fullString;
		this.notRecognizedString = notRecognizedString;
	}

	public String getFullString() {
		return fullString;
	}

	public String getNotRecognizedString() {
		return notRecognizedString;
	}
}
