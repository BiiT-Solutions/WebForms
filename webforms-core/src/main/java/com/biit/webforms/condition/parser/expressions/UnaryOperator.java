package com.biit.webforms.condition.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.math.domain.FlowDomain;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.ITokenType;

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
			// TODO
			throw new RuntimeException("Not implemented");
		}
	}

	@Override
	public Expression negate() {
		if (type == TokenTypes.NOT && prefix) {
			// The negation of !(A) is (A)
			return expression;
		} else {
			// TODO
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
	public FlowDomain getDomain() {
		FlowDomain domain = ((WebformsExpression) expression).getDomain();
		if (type == TokenTypes.NOT) {
			domain = domain.inverse();
		}
		return domain;
	}
}
