package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.ExpectedTokenNotFound;
import com.biit.webforms.condition.parser.Parser;
import com.biit.webforms.condition.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.condition.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.condition.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.condition.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.condition.parser.exceptions.ParseException;
import com.biit.webforms.condition.parser.expressions.BinaryOperator;
import com.biit.webforms.condition.parser.expressions.Expression;
import com.biit.webforms.persistence.entity.condition.Token;

public class BinaryOperationParselet implements InfixParselet {

	private final int precedence;
	private final boolean rightAsociation;

	public BinaryOperationParselet(int precedence, boolean rightAsociation) {
		this.precedence = precedence;
		this.rightAsociation = rightAsociation;
	}

	@Override
	public Expression parse(Parser parser, Expression left, Token token) throws ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		// To handle right-associative operators like "^", we allow a slightly
		// lower precedence when parsing the right-hand side. This will let a
		// parselet with the same precedence appear on the right, which will
		// then take *this* parselet's result as its left-hand argument.
		try{
			Expression right = parser.parseExpression(precedence - (rightAsociation ? 1 : 0));
			if(right==null){
				throw new IncompleteBinaryOperatorException(token);
			}
			return new BinaryOperator(left, token.getType(), right);
		} catch(ParseException e){
			throw new IncompleteBinaryOperatorException(token);
		}		
	}

	@Override
	public int getPrecedence() {
		return precedence;
	}

}
