package com.biit.webforms.condition.parser;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Iterator;

import com.biit.webforms.condition.parser.parselets.BetweenParselet;
import com.biit.webforms.condition.parser.parselets.BinaryOperationParselet;
import com.biit.webforms.condition.parser.parselets.ComparationClusterParselet;
import com.biit.webforms.condition.parser.parselets.EmptyParselet;
import com.biit.webforms.condition.parser.parselets.InParselet;
import com.biit.webforms.condition.parser.parselets.ParenthesisParselet;
import com.biit.webforms.condition.parser.parselets.PrefixOperatorParselet;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Parser;

/**
 * Extended PRATT parser class for webforms condition expressions. 
 *
 */
public class WebformsParser extends Parser {

	/**
	 * Initialize the parser with a list of tokens to parse.
	 * @param tokens
	 */
	public WebformsParser(Iterator<Token> tokens) {
		super(tokens);
		configure();
	}

	private void configure() {
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
		register(TokenTypes.IN, new InParselet());
		register(TokenTypes.BETWEEN, new BetweenParselet());
		//registerTokenToSkip(TokenTypes.EMPTY);
		register(TokenTypes.EMPTY, new EmptyParselet());
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
