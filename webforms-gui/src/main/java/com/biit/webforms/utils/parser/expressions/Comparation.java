package com.biit.webforms.utils.parser.expressions;

import com.biit.webforms.utils.lexer.ITokenType;
import com.biit.webforms.utils.lexer.TokenTypes;

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

}
