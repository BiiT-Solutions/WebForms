package com.biit.webforms.condition.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.ITokenType;

/**
 * Expressions [EXPR] and [EXPR]
 * 
 * @author joriz_000
 * 
 */
public class BinaryOperator extends Expression implements WebformsExpression {

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

	@Override
	public Expression flatten() {
		// This can't be flattened. We pass the flattening to each part.
		return new BinaryOperator(left.flatten(), type, right.flatten());
	}

	@Override
	public Expression negate() {
		if (type == TokenTypes.AND) {
			return new BinaryOperator(left.negate(), TokenTypes.OR, right.negate());
		}
		if (type == TokenTypes.OR) {
			return new BinaryOperator(left.negate(), TokenTypes.AND, right.negate());
		}
		// TODO anything else is unexpected.
		throw new RuntimeException("unexpected");
	}

	@Override
	public List<Expression> getAll(Class<?> arg0) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if (arg0.isInstance(this)) {
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

	// @Override
	// public FlowDomain getDomain() {
	// FlowDomain leftDomain = ((WebformsExpression)left).getDomain();
	// FlowDomain rightDomain = ((WebformsExpression)right).getDomain();
	//
	// if(type == TokenTypes.AND){
	// return leftDomain.intersection(rightDomain);
	// }else{
	// return leftDomain.union(rightDomain);
	// }
	// }

	@Override
	public IDomain getDomain() {
		IDomain leftDomain = ((WebformsExpression) left).getDomain();
		IDomain rightDomain = ((WebformsExpression) right).getDomain();
		System.out.println("Binary operator, left :"+leftDomain+" right:"+rightDomain); 

		if (type == TokenTypes.AND) {
			System.out.println("Intersec: "+leftDomain.intersect(rightDomain));
			return leftDomain.intersect(rightDomain);
		} else {
			System.out.println("Union: "+leftDomain.union(rightDomain));
			return leftDomain.union(rightDomain);
		}
	}
}
