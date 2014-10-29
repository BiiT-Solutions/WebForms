package com.biit.webforms.condition.parser.expressions;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.ITokenType;

public class UnaryOperator extends Expression {

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
}
