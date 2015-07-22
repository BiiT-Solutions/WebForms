package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.expressions.UnaryOperator;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.parselets.PrefixParselet;


/**
 * Generic prefix operator for unary operators in prefix. Parses unary '!a'
 * expressions
 * 
 */
public class PrefixOperatorParselet implements PrefixParselet {

	private int precedence;

	public PrefixOperatorParselet(int precedence) {
		this.precedence = precedence;
	}

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		// To handle right-associative operators like "^", we allow a slightly
		// lower precedence when parsing the right-hand side. This will let a
		// parselet with the same precedence appear on the right, which will
		// then take *this* parselet's result as its left-hand argument.
		Expression right = parser.parseExpression(precedence);

		return new UnaryOperator(token.getType(), right);
	}

	public int getPrecedence() {
		return precedence;
	}

}
