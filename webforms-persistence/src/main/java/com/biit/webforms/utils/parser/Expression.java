package com.biit.webforms.utils.parser;

import com.biit.webforms.persistence.entity.condition.Token;

import java.util.List;

/**
 * Interface for all expression recognized by the PRATT parser. 
 *
 */
public abstract class Expression {

	public abstract void getString(StringBuilder builder);
	
	/**
	 * Creates a copy of current expression "flattened"
	 * @return returns the expression
	 */
	public abstract Expression flatten();
	
	/**
	 * Returns a copy of current expression negated.
	 * @return returns the expression
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
	 * @param clazz the filter
	 * @return returns the expressions
	 */
	public abstract List<Expression> getAll(Class<?> clazz);
	
	/**
	 * Returns all token clazz elements
	 * @param tokenClazz the filter
	 * @return returns the tokens
	 */
	public abstract List<Token> getAllTokens(Class<? extends Token> tokenClazz);
}
