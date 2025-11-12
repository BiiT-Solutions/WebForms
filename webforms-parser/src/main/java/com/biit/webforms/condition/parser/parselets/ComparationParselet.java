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

import com.biit.webforms.condition.parser.Precedence;
import com.biit.webforms.condition.parser.expressions.Comparation;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.parselets.InfixParselet;

/**
 * Parselet that implements the parsing of elements when a comparation is
 * present in a list of tokens.
 *
 */
public class ComparationParselet implements InfixParselet {

	@Override
	public Expression parse(Parser parser, Expression left, Token token) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		Expression right = parser.parseExpression(Precedence.CONDITIONAL - 1);

		return new Comparation(left, token.getType(), right);
	}

	@Override
	public int getPrecedence() {
		return Precedence.CONDITIONAL;
	}

}
