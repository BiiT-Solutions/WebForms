package com.biit.webforms.utils.parser.expressions;

import com.biit.webforms.utils.lexer.ITokenType;
import com.biit.webforms.utils.lexer.WebformsTokenTypes;

/**
 * Expressions [EXPR] and [EXPR]
 * 
 * @author joriz_000
 * 
 */
public class BinaryOperator extends Expression {

	private Expression left;
	private WebformsTokenTypes type;
	private Expression right;

	public BinaryOperator(Expression left, ITokenType type, Expression right) {
		this.left = left;
		this.type = (WebformsTokenTypes) type;
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
