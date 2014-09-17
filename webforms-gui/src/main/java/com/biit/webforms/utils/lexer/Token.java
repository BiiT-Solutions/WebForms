package com.biit.webforms.utils.lexer;

public abstract class Token {

	private String originalString;

	public String getOriginalString() {
		return originalString;
	}

	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}
}
