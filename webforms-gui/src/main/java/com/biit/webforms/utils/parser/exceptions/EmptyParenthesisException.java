package com.biit.webforms.utils.parser.exceptions;

import com.biit.webforms.utils.lexer.Token;

public class EmptyParenthesisException extends Exception {
	private static final long serialVersionUID = 7737638776733320226L;

	private Token token;

	public EmptyParenthesisException(Token token) {
		super("Empty parenthesis in position " + token.getPosition());
		this.setToken(token);
	}

	public Token getToken() {
		return token;
	}

	private void setToken(Token token) {
		this.token = token;
	}

}
