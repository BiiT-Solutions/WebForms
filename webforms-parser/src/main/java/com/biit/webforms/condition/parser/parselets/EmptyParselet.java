package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.expressions.EmptyFunction;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenEmpty;
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
 * This parserlet implements how to parse an empty token.
 *
 */
public class EmptyParselet implements PrefixParselet {

	@Override
	public Expression parse(Parser parser, Token token)
			throws ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException,
			MissingParenthesisException, EmptyParenthesisException {
		try {
			return new EmptyFunction((TokenEmpty) token);
		} catch (ClassCastException e) {
			throw new ExpectedTokenNotFound("Only 'TokenEmpty' allowed on EmptyFunction");
		}
	}

}
