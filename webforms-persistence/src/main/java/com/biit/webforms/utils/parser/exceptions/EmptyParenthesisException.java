package com.biit.webforms.utils.parser.exceptions;

import com.biit.webforms.persistence.entity.condition.Token;

public class EmptyParenthesisException extends Exception {
	private static final long serialVersionUID = 7737638776733320226L;

	private Token token;

	public EmptyParenthesisException(Token token) {
		super("Empty parenthesis in " + token);
		this.setToken(token);
	}

	public Token getToken() {
		return token;
	}

	private void setToken(Token token) {
		this.token = token;
	}

}
