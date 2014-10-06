package com.biit.webforms.condition.parser.exceptions;

import com.biit.webforms.persistence.entity.condition.Token;

public class ExpressionNotWellFormedException extends Exception {
	private static final long serialVersionUID = 1282611261622567801L;

	private Token token;
	
	public ExpressionNotWellFormedException(Token token) {
		super("Expression not well formed since: "+token);
		this.setToken(token);
	}

	public Token getToken() {
		return token;
	}

	private void setToken(Token token) {
		this.token = token;
	}
}
