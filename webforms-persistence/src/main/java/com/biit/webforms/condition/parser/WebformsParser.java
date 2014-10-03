package com.biit.webforms.condition.parser;

import java.util.Iterator;

import com.biit.webforms.condition.parser.parselets.BinaryOperationParselet;
import com.biit.webforms.condition.parser.parselets.ComparationParselet;
import com.biit.webforms.condition.parser.parselets.ParenthesisParselet;
import com.biit.webforms.condition.parser.parselets.PrefixOperatorParselet;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;

public class WebformsParser extends Parser {

	public WebformsParser(Iterator<Token> tokens) {
		super(tokens);

		// Configure the parser
		// Register prefix parselets
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
