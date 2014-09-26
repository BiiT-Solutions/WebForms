package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;

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
			IncompleteBinaryOperatorException, MissingParenthesisException;

}
