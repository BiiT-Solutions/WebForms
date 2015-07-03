package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.expressions.InFunction;
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
 * Generates InFunction expression when In token is detected.
 *
 */
public class InParselet implements PrefixParselet{

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException,
			EmptyParenthesisException {
		return new InFunction(token);
	}

}
