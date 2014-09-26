package com.biit.webforms.utils.parser.exceptions;

import com.biit.webforms.utils.lexer.Token;

public class MissingParenthesisException extends Exception {
	private static final long serialVersionUID = -4193342609183777176L;

	public MissingParenthesisException(Token token) {
		super("Parenthesis starting at "+token.getPosition()+" is not closed");
	}
}
