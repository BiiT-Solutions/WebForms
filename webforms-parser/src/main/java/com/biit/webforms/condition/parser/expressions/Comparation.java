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

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.ITokenType;

/**
 * Expression that holds a comparation where [LEFT] eq/ne [RIGHT]
 *
 */
public class Comparation extends Expression {

	private Expression left;
	private TokenTypes type;
	private Expression right;

	public Comparation(Expression left, ITokenType type, Expression right) {
		this.left = left;
		this.type = (TokenTypes) type;
		this.right = right;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append("(");
		left.getString(builder);
		builder.append(" ").append(type).append(" ");
		right.getString(builder);
		builder.append(")");
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	@Override
	public Expression flatten() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Expression negate() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public List<Expression> getAll(Class<?> arg0) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if(arg0.isInstance(this)){
			retrieved.add(this);
		}
		retrieved.addAll(left.getAll(arg0));
		retrieved.addAll(right.getAll(arg0));
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> arg0) {
		List<Token> retrieved = new ArrayList<Token>();
			
		retrieved.addAll(left.getAllTokens(arg0));
		retrieved.addAll(right.getAllTokens(arg0));
		
		return retrieved;
	}
}
