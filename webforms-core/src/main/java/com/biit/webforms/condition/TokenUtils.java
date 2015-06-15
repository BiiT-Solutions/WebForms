package com.biit.webforms.condition;

import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.condition.Token;

public class TokenUtils {

	/**
	 * The condition must have a pair of parenthesis to be joined to another rules.
	 * 
	 * @param condition
	 * @return
	 */
	public static boolean needsEnclosingParenthesis(List<Token> condition) {
		// If it Has a Or or And, does not starts with a parenthesis.
		return isComposedPredicate(condition) && ((!condition.get(0).getType().equals(TokenTypes.LEFT_PAR)
		// The ending parenthesis does not match with the starting one.
				|| condition.indexOf(getClosingParenthesis(condition.get(0), condition)) != condition.size() - 1));
	}

	public static boolean isComposedPredicate(List<Token> condition) {
		if (condition != null) {
			for (Token token : condition) {
				if (isLogicalOperator(token)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns if the token is a AND or a OR.
	 * 
	 * @param token
	 * @return
	 */
	public static boolean isLogicalOperator(Token token) {
		return token.getType().equals(TokenTypes.AND) || token.getType().equals(TokenTypes.OR);
	}

	/**
	 * Return the right parenthesis that closes a given left parenthesis.
	 * 
	 * @param leftParenthesis
	 * @param condition
	 * @return
	 */
	public static Token getClosingParenthesis(Token leftParenthesis, List<Token> condition) {
		if (!condition.contains(leftParenthesis)) {
			return null;
		}
		int parenthesisCounter = 0;
		for (Token token : condition.subList(condition.indexOf(leftParenthesis), condition.size())) {
			if (token.getType().equals(TokenTypes.LEFT_PAR)) {
				parenthesisCounter++;
			}
			if (token.getType().equals(TokenTypes.RIGHT_PAR)) {
				parenthesisCounter--;
				if (parenthesisCounter == 0) {
					return token;
				}
			}
		}
		return null;
	}
}
