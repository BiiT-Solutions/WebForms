package com.biit.webforms.condition.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
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
