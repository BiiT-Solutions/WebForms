package com.biit.webforms.utils.parser.parselets;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.expressions.Reference;

public class ReferenceParselet implements PrefixParselet {

	@Override
	public Expression parse(Parser parser, Token token) {
		List<Token> tokens = new ArrayList<>();
		tokens.add(token);
		while (true) {
			// Consume references until there is no more references
			try {
				Token nextReference = parser.consume(TokenTypes.REFERENCE);
				tokens.add(nextReference);
			} catch (ExpectedTokenNotFound | NoMoreTokensException e) {
				break;
			}
		}
		return new Reference(tokens);
	}

}
