package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.expressions.ComparationCluster;
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
 * Parselet that implements the parsing of clustered comparations in a expression
 */
public class ComparationClusterParselet implements PrefixParselet {

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException,
			EmptyParenthesisException {
		return new ComparationCluster(token);
	}

}
