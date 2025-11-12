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

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.ITokenType;

/**
 * Expression for unary operations. In this moment the only unary operation is
 * NOT[EXPR]
 *
 */
public class UnaryOperator extends Expression implements WebformsExpression {

	private boolean prefix;
	private TokenTypes type;
	private Expression expression;

	public UnaryOperator(ITokenType type, Expression right) {
		this.type = (TokenTypes) type;
		this.expression = right;
		this.prefix = true;
	}

	public UnaryOperator(Expression left, ITokenType type) {
		this.type = (TokenTypes) type;
		this.expression = left;
		this.prefix = false;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append("(");
		if (prefix) {
			builder.append(type);
			builder.append(" ");
			expression.getString(builder);
		} else {
			expression.getString(builder);
			builder.append(" ");
			builder.append(type);
		}
		builder.append(")");
	}

	@Override
	public Expression flatten() {
		if (type == TokenTypes.NOT && prefix) {
			return expression.negate();
		} else {
			throw new RuntimeException("Not implemented");
		}
	}

	@Override
	public Expression negate() {
		if (type == TokenTypes.NOT && prefix) {
			// The negation of !(A) is (A)
			return expression;
		} else {
			throw new RuntimeException("Not implemented");
		}
	}

	@Override
	public List<Expression> getAll(Class<?> arg0) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if (arg0.isInstance(this)) {
			retrieved.add(this);
		}
		retrieved.addAll(expression.getAll(arg0));
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> arg0) {
		List<Token> retrieved = new ArrayList<Token>();

		retrieved.addAll(expression.getAllTokens(arg0));

		return retrieved;
	}

	@Override
	public IDomain getDomain() {
		IDomain domain = ((WebformsExpression) expression).getDomain();
		if (type == TokenTypes.NOT) {
			domain = domain.inverse();
		}
		return domain;
	}

	@Override
	public boolean checkBlockByMinTerms(Form form, BaseQuestion element) {
		return ((WebformsExpression) expression).checkBlockByMinTerms(form, element);
	}

	@Override
	public Boolean evaluate() {
		Boolean value = ((WebformsExpression) expression).evaluate();
		if(value == null){
			return null;
		}
		
		if (type == TokenTypes.NOT){
			return !value;
		}
		return value;
	}
}
