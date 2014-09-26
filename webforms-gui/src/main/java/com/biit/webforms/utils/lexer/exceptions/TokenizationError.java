package com.biit.webforms.utils.lexer.exceptions;

public class TokenizationError extends Exception{
	private static final long serialVersionUID = -8295221891149017954L;
	
	public TokenizationError(String currentText) {
		super("'"+currentText+"' can't be matched to a valid expression");
	}
}
