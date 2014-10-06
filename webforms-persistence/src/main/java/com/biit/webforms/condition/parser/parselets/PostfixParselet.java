package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.Parser;
import com.biit.webforms.condition.parser.exceptions.ParseException;
import com.biit.webforms.condition.parser.expressions.Expression;
import com.biit.webforms.condition.parser.expressions.UnaryOperator;
import com.biit.webforms.persistence.entity.condition.Token;

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
