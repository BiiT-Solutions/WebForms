package com.biit.webforms.exporters.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.Token;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

public class BooleanExpressionSimplifier {
	private List<Token> expression;
	private HashMap<String, Token> tokens;

	public BooleanExpressionSimplifier(List<Token> expression) {
		this.expression = expression;
		tokens = new HashMap<>();

		// Add basic operations
		for (TokenTypes type : TokenTypes.values()) {
			Token token = Token.getToken(type);
			tokens.put(token.getExpressionSimplifierRepresentation(), token);
		}
	}

	/**
	 * Return a simplified version of the expression.
	 *
	 * @return
	 */
	public List<Token> getSimplified() {
		String expressionAsString = "";
		// Convert the expression to string to be used by the ExprParser;
		for (Token token : expression) {
			expressionAsString += token.getExpressionSimplifierRepresentation();
			tokens.put(token.getExpressionSimplifierRepresentation(), token);
		}

		Expression<String> parsedExpression = ExprParser.parse(expressionAsString);
		Expression<String> simplified = RuleSet.simplify(parsedExpression);

		// True is the same that empty list.
		if (simplified.toString().equals("true")) {
			return new ArrayList<Token>();
		}

		// Convert back string to tokens.
		List<Token> result = new ArrayList<>();
		// We move 'not' to have an space to be split also a new token.
		String[] newTokens = simplified.toString().replace("!", " ! ").replace("(", " ( ").replace(")", " ) ")
				.replace("  ", " ").split(" ");

		int leftParenthesisAdded = 0;
		for (int i = 0; i < newTokens.length; i++) {
			// Recover token.
			Token token = tokens.get(newTokens[i]);
			if (token != null) {
				result.add(token);
				// 'not' parenthesis are removed when simplifying, but are needed in orbeon. Add it again if does not
				// exists.
				if (token.getType().equals(TokenTypes.NOT)) {
					// Not exist a parenthesis.
					if (i < newTokens.length - 1 && !tokens.get(newTokens[i + 1]).getType().equals(TokenTypes.LEFT_PAR)) {
						result.add(Token.getLeftParenthesisToken());
						leftParenthesisAdded++;
					}
				} else {
					while (leftParenthesisAdded > 0) {
						result.add(Token.getRigthParenthesisToken());
						leftParenthesisAdded--;
					}
				}
			} else {
				WebformsLogger.severe(this.getClass().getName(),
						"Token not found! " + newTokens[i] + " -> " + tokens.get(newTokens[i]));
			}
		}

		return result;
	}
}
