package com.biit.webforms.utils.parser;

import java.util.List;

import com.biit.webforms.persistence.entity.condition.Token;

public abstract class Expression {

	public abstract void getString(StringBuilder builder);
	
	/**
	 * Creates a copy of current expression "flattened"
	 * @return
	 */
	public abstract Expression flatten();
	
	/**
	 * Returns a copy of current expression negated.
	 * @return
	 */
	public abstract Expression negate();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		getString(builder);
		return builder.toString();
	}
	
	/**
	 * Returns all clazz members in the expression 
	 * @param clazz
	 * @return
	 */
	public abstract List<Expression> getAll(Class<?> clazz);
	
	/**
	 * Returns all token clazz elements
	 * @param tokenClazz
	 * @return
	 */
	public abstract List<Token> getAllTokens(Class<? extends Token> tokenClazz);
}
