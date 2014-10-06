package com.biit.webforms.condition.parser.expressions;

import com.biit.webforms.condition.parser.ITokenType;
import com.biit.webforms.enumerations.TokenTypes;

/**
 * Expressions [EXPR] and [EXPR]
 * 
 * @author joriz_000
 * 
 */
public class BinaryOperator extends Expression {

	private Expression left;
	private TokenTypes type;
	private Expression right;

	public BinaryOperator(Expression left, ITokenType type, Expression right) {
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

}
