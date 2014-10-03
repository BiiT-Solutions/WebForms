package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.ExpectedTokenNotFound;
import com.biit.webforms.condition.parser.Parser;
import com.biit.webforms.condition.parser.Precedence;
import com.biit.webforms.condition.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.condition.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.condition.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.condition.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.condition.parser.exceptions.ParseException;
import com.biit.webforms.condition.parser.expressions.Comparation;
import com.biit.webforms.condition.parser.expressions.Expression;
import com.biit.webforms.persistence.entity.condition.Token;

public class ComparationParselet implements InfixParselet {

	@Override
	public Expression parse(Parser parser, Expression left, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException,
			EmptyParenthesisException {
		Expression right = parser.parseExpression(Precedence.CONDITIONAL - 1);

		return new Comparation(left, token.getType(), right);
	}

	@Override
	public int getPrecedence() {
		return Precedence.CONDITIONAL;
	}

}
