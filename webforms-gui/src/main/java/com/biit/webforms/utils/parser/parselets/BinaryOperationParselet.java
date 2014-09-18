package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.BinaryOperator;
import com.biit.webforms.utils.parser.expressions.Expression;

public class BinaryOperationParselet implements InfixParselet {

	private final int precedence;
	private final boolean rightAsociation;

	public BinaryOperationParselet(int precedence, boolean rightAsociation) {
		this.precedence = precedence;
		this.rightAsociation = rightAsociation;
	}

	@Override
	public Expression parse(Parser parser, Expression left, Token token) throws ParseException, ExpectedTokenNotFound {
		// To handle right-associative operators like "^", we allow a slightly
		// lower precedence when parsing the right-hand side. This will let a
		// parselet with the same precedence appear on the right, which will
		// then take *this* parselet's result as its left-hand argument.
		Expression right = parser.parseExpression(precedence - (rightAsociation ? 1 : 0));
		return new BinaryOperator(left, token.getType(), right);
	}

	@Override
	public int getPrecedence() {
		return precedence;
	}

}
