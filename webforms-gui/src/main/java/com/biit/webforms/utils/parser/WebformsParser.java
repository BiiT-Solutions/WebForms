package com.biit.webforms.utils.parser;

import java.util.Iterator;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.WebformsTokenTypes;
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
		register(WebformsTokenTypes.REFERENCE, new ReferenceParselet());
		register(WebformsTokenTypes.TEXT, new TextParselet());
		register(WebformsTokenTypes.NOT, new PrefixOperatorParselet(Precedence.PREFIX));
		// Register infix parselets
		// Grouping
		register(WebformsTokenTypes.LEFT_PAR, new ParenthesisParselet());
		// Comparations
		register(WebformsTokenTypes.GE, new ComparationParselet());
		register(WebformsTokenTypes.LE, new ComparationParselet());
		register(WebformsTokenTypes.GT, new ComparationParselet());
		register(WebformsTokenTypes.LT, new ComparationParselet());
		register(WebformsTokenTypes.EQ, new ComparationParselet());
		register(WebformsTokenTypes.NE, new ComparationParselet());
		// LOGIC
		register(WebformsTokenTypes.AND, new BinaryOperationParselet(Precedence.AND, false));
		register(WebformsTokenTypes.OR, new BinaryOperationParselet(Precedence.OR, false));

		// Register ignore
		registerTokenToSkip(WebformsTokenTypes.WHITESPACE);
	}

}
