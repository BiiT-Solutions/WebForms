package com.biit.webforms.condition.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;

public class ComparationCluster extends Expression {

	private Token token;

	public ComparationCluster(Token token) {
		this.token = token;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.toString());
	}

	@Override
	public Expression flatten() {
		// Comparation cluster can't be flattened is already a simple
		// expression.
		return new ComparationCluster(token);
	}

	@Override
	public Expression negate() {
		return new ComparationCluster(token.inverse());
	}

	@Override
	public List<Expression> getAll(Class<?> arg0) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if(arg0.isInstance(this)){
			retrieved.add(this);
		}
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> arg0) {
		List<Token> retrieved = new ArrayList<Token>();
		if(arg0.isInstance(token)){
			retrieved.add(token);
		}
		return retrieved;
	}

}
