package com.biit.webforms.condition.parser;

import java.util.Iterator;

import com.biit.webforms.condition.parser.parselets.BinaryOperationParselet;
import com.biit.webforms.condition.parser.parselets.ComparationClusterParselet;
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
		// Comparation cluster
		register(TokenTypes.GE, new ComparationClusterParselet());
		register(TokenTypes.LE, new ComparationClusterParselet());
		register(TokenTypes.GT, new ComparationClusterParselet());
		register(TokenTypes.LT, new ComparationClusterParselet());
		register(TokenTypes.EQ, new ComparationClusterParselet());
		register(TokenTypes.NE, new ComparationClusterParselet());
		// Grouping
		register(TokenTypes.LEFT_PAR, new ParenthesisParselet());
		
		// Register infix parselets
		// LOGIC
		register(TokenTypes.AND, new BinaryOperationParselet(Precedence.AND, false));
		register(TokenTypes.OR, new BinaryOperationParselet(Precedence.OR, false));

		// Register ignore
		registerTokenToSkip(TokenTypes.WHITESPACE);
		registerTokenToSkip(TokenTypes.RETURN);
	}

}
