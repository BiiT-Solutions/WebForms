package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;

public class ParenthesisParselet implements PrefixParselet{

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException {
		Expression expression = parser.parseExpression();
		try{
			parser.consume(TokenTypes.RIGHT_PAR);
		}catch(NoMoreTokensException e){
			throw new MissingParenthesisException(token);
		}
	    return expression;
	}

}
