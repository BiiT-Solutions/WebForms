package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.ExpectedTokenNotFound;
import com.biit.webforms.condition.parser.Parser;
import com.biit.webforms.condition.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.condition.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.condition.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.condition.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.condition.parser.exceptions.ParseException;
import com.biit.webforms.condition.parser.expressions.Expression;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;

public class ParenthesisParselet implements PrefixParselet {

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException,
			EmptyParenthesisException {
		if (parser.lookAhead(0) == null) {
			throw new MissingParenthesisException(token);
		}
		if (parser.lookAhead(0).getType() == TokenTypes.RIGHT_PAR) {
			throw new EmptyParenthesisException(token);
		}
		
		Expression expression = parser.parseExpression();

		try {
			parser.consume(TokenTypes.RIGHT_PAR);
		} catch (NoMoreTokensException e) {
			throw new MissingParenthesisException(token);
		}
		return expression;
	}

}
