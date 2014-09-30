package com.biit.webforms.utils.parser.exceptions;

import com.biit.webforms.utils.lexer.Token;

public class ExpressionNotWellFormedException extends Exception {
	private static final long serialVersionUID = 1282611261622567801L;

	private Token token;
	
	public ExpressionNotWellFormedException(Token token) {
		super("Expression not well formed since: "+token.getPosition());
		this.setToken(token);
	}

	public Token getToken() {
		return token;
	}

	private void setToken(Token token) {
		this.token = token;
	}
}
