package com.biit.webforms.condition.parser.exceptions;

import com.biit.webforms.persistence.entity.condition.Token;

public class MissingParenthesisException extends Exception {
	private static final long serialVersionUID = -4193342609183777176L;

	public MissingParenthesisException(Token token) {
		super("Parenthesis starting at "+token+" is not closed");
	}
}
