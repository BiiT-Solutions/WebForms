package com.biit.webforms.utils.parser;

import java.util.Iterator;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.biit.webforms.utils.parser.parselets.BinaryOperationParselet;
import com.biit.webforms.utils.parser.parselets.ComparationParselet;
import com.biit.webforms.utils.parser.parselets.ParenthesisParselet;
import com.biit.webforms.utils.parser.parselets.PrefixOperatorParselet;
import com.biit.webforms.utils.parser.parselets.ReferenceParselet;
import com.biit.webforms.utils.parser.parselets.TextParselet;

public class WebformsParser extends Parser {

	public WebformsParser(Iterator<Token> tokens) {
		super(tokens);
		// Register prefix parselets
		register(TokenTypes.REFERENCE, new ReferenceParselet());
		register(TokenTypes.TEXT, new TextParselet());
		register(TokenTypes.NOT, new PrefixOperatorParselet(Precedence.PREFIX));
		// Register infix parselets
		// Grouping
		register(TokenTypes.LEFT_PAR, new ParenthesisParselet());
		// Comparations
		register(TokenTypes.GE, new ComparationParselet());
		register(TokenTypes.LE, new ComparationParselet());
		register(TokenTypes.GT, new ComparationParselet());
		register(TokenTypes.LT, new ComparationParselet());
		register(TokenTypes.EQ, new ComparationParselet());
		register(TokenTypes.NE, new ComparationParselet());
		// LOGIC
		register(TokenTypes.AND, new BinaryOperationParselet(Precedence.AND, false));
		register(TokenTypes.OR, new BinaryOperationParselet(Precedence.OR, false));

		// Register ignore
		registerTokenToSkip(TokenTypes.WHITESPACE);
	}

}
