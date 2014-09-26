package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.expressions.Value;

public class TextValueParselet implements PrefixParselet{

	@Override
	public Expression parse(Parser parser, Token token) {
		return new Value(token);
	}

}
