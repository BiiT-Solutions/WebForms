package com.biit.webforms.condition.parser.parselets;

import com.biit.webforms.condition.parser.ExpectedTokenNotFound;
import com.biit.webforms.condition.parser.Parser;
import com.biit.webforms.condition.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.condition.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.condition.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.condition.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.condition.parser.exceptions.ParseException;
import com.biit.webforms.condition.parser.expressions.Expression;
import com.biit.webforms.persistence.entity.condition.Token;


/**
 * 
 * A PrefixParselet is associated with a token that appears at the beginning of
 * an expression. Its parse() method will be called with the consumed leading
 * token, and the parselet is responsible for parsing anything that comes after
 * that token. This interface is also used for single-token expressions like
 * variables, in which case parse() simply doesn't consume any more tokens.
 * 
 */
public interface PrefixParselet {

	Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException;

}
