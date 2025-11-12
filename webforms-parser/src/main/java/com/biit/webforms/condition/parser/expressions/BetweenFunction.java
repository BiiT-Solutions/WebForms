package com.biit.webforms.condition.parser.expressions;

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

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;

/**
 * Expression that encapsulates a token of type between
 *
 */
public class BetweenFunction extends Expression {

	private Token token;

	public BetweenFunction(Token token) {
		this.token = token;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.toString());
	}

	@Override
	public Expression flatten() {
		return null;
	}

	@Override
	public Expression negate() {
		return null;
	}

	@Override
	public List<Expression> getAll(Class<?> clazz) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if (clazz.isInstance(this)) {
			retrieved.add(this);
		}
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> tokenClazz) {
		List<Token> retrieved = new ArrayList<Token>();
		if (tokenClazz.isInstance(token)) {
			retrieved.add(token);
		}
		return retrieved;
	}

}
