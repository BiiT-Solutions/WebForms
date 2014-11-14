package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
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
			result.add(token);
			// 'not' parenthesis are removed when simplifying, but are needed in orbeon. Add it again.
			if (token.getType().equals(TokenTypes.NOT)) {
				result.add(Token.leftPar());
				leftParenthesisAdded++;
			} else {
				while (leftParenthesisAdded > 0) {
					result.add(Token.rigthPar());
					leftParenthesisAdded--;
				}
			}
		}

		return result;
	}
}
