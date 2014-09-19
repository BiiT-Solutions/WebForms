package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;

public class ParenthesisParselet implements PrefixParselet{

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound {
		Expression expression = parser.parseExpression();
	    parser.consume(TokenTypes.RIGHT_PAR);
	    return expression;
	}

}
