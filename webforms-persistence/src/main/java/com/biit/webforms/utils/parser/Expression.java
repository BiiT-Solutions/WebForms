package com.biit.webforms.utils.parser;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
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

import com.biit.webforms.persistence.entity.condition.Token;

import java.util.List;

/**
 * Interface for all expression recognized by the PRATT parser. 
 *
 */
public abstract class Expression {

	public abstract void getString(StringBuilder builder);
	
	/**
	 * Creates a copy of current expression "flattened"
	 * @return returns the expression
	 */
	public abstract Expression flatten();
	
	/**
	 * Returns a copy of current expression negated.
	 * @return returns the expression
	 */
	public abstract Expression negate();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		getString(builder);
		return builder.toString();
	}
	
	/**
	 * Returns all clazz members in the expression 
	 * @param clazz the filter
	 * @return returns the expressions
	 */
	public abstract List<Expression> getAll(Class<?> clazz);
	
	/**
	 * Returns all token clazz elements
	 * @param tokenClazz the filter
	 * @return returns the tokens
	 */
	public abstract List<Token> getAllTokens(Class<? extends Token> tokenClazz);
}
