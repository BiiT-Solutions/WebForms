package com.biit.webforms.utils.parser.expressions;

import com.biit.webforms.utils.lexer.Token;

public class Value extends Expression {

	private Token token;

	public Value(Token token) {
		this.token = token;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.getContent());
	}

}
