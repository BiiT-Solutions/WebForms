package com.biit.webforms.condition;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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

	public static boolean isParenthesis(Token token) {
		return token.getType().equals(TokenTypes.LEFT_PAR) || token.getType().equals(TokenTypes.RIGHT_PAR);
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
