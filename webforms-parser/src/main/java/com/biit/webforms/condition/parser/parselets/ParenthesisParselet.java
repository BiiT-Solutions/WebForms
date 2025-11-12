package com.biit.webforms.condition.parser.parselets;

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

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.parselets.PrefixParselet;

/**
 * Parselet that implements the parsing of elements when a parenthesis is found
 * in a list of tokens
 *
 */
public class ParenthesisParselet implements PrefixParselet {

	@Override
	public Expression parse(Parser parser, Token token) throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		if (parser.lookAhead(0) == null) {
			throw new MissingParenthesisException(token);
		}
		if (parser.lookAhead(0).getType() == TokenTypes.RIGHT_PAR) {
			throw new EmptyParenthesisException(token);
		}

		Expression expression = parser.parseExpression();

		try {
			parser.consume(TokenTypes.RIGHT_PAR);
		} catch (NoMoreTokensException e) {
			throw new MissingParenthesisException(token);
		}
		return expression;
	}

}
