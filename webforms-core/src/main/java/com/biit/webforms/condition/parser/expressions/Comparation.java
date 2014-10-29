package com.biit.webforms.condition.parser.expressions;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.ITokenType;

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

}
