package com.biit.webforms.utils.parser.parselets;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.expressions.Reference;

public class ReferenceParselet implements PrefixParselet{

	@Override
	public Expression parse(Parser parser, Token token) {
		return new Reference(token);
	}

}
