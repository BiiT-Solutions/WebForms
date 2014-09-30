package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;

/**
 * 
 * An InfixParselet is associated with a token that appears in the middle of the
 * expression it parses. Its parse() method will be called after the left-hand
 * side has been parsed, and it in turn is responsible for parsing everything
 * that comes after the token. This is also used for postfix expressions, in
 * which case it simply doesn't consume any more tokens in its parse() call.
 * 
 */
public interface InfixParselet {
	Expression parse(Parser parser, Expression left, Token token) throws ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException;

	int getPrecedence();
}
