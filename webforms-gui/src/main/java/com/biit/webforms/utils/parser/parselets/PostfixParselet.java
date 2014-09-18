package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.expressions.UnaryOperator;

public class PostfixParselet implements InfixParselet {
	private final int precedence;

	public PostfixParselet(int precedence) {
		this.precedence = precedence;
	}

	@Override
	public Expression parse(Parser parser, Expression left, Token token) throws ParseException {
		return new UnaryOperator(left, token.getType());
	}

	public int getPrecedence() {
		return precedence;
	}

}
