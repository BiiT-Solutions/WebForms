package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.expressions.Value;

public class AnswerValueParselet implements PrefixParselet {

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException {
		return new Value(token);
	}

}
