package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.Precedence;
import com.biit.webforms.condition.parser.expressions.Comparation;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.parselets.InfixParselet;

/**
 * Parselet that implements the parsing of elements when a comparation is
 * present in a list of tokens.
 *
 */
public class ComparationParselet implements InfixParselet {

	@Override
	public Expression parse(Parser parser, Expression left, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		Expression right = parser.parseExpression(Precedence.CONDITIONAL - 1);

		return new Comparation(left, token.getType(), right);
	}

	@Override
	public int getPrecedence() {
		return Precedence.CONDITIONAL;
	}

}
