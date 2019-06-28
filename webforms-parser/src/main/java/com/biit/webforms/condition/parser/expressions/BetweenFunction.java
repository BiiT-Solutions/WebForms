package com.biit.webforms.condition.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.parser.Expression;

/**
 * Expression that encapsulates a token of type between
 *
 */
public class BetweenFunction extends Expression {

	private Token token;

	public BetweenFunction(Token token) {
		this.token = token;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.toString());
	}

	@Override
	public Expression flatten() {
		return null;
	}

	@Override
	public Expression negate() {
		return null;
	}

	@Override
	public List<Expression> getAll(Class<?> clazz) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if (clazz.isInstance(this)) {
			retrieved.add(this);
		}
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> tokenClazz) {
		List<Token> retrieved = new ArrayList<Token>();
		if (tokenClazz.isInstance(token)) {
			retrieved.add(token);
		}
		return retrieved;
	}

}
