package com.biit.webforms.condition.parser.exceptions;

import com.biit.webforms.persistence.entity.condition.Token;

public class IncompleteBinaryOperatorException extends Exception {
	private static final long serialVersionUID = -6226709058899098687L;

	private Token token;
	
	public IncompleteBinaryOperatorException(Token token) {
		super("Binary operator "+token+" at "+ token+" is incomplete");
		this.setToken(token);
	}

	public Token getToken() {
		return token;
	}

	private void setToken(Token token) {
		this.token = token;
	}
}
