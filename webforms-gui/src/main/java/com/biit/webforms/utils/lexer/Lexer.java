package com.biit.webforms.utils.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.biit.webforms.utils.lexer.exceptions.TokenizationError;

public class Lexer {

	// Token list by precedence.
	private List<List<ITokenDefinition>> tokenDefinitions;

	public Lexer() {
		tokenDefinitions = new ArrayList<List<ITokenDefinition>>();
	}

	public void addTokenDefinition(ITokenDefinition tokenType) {
		// Initialize with empty lists if there is no token with that
		// precendence level
		while (tokenDefinitions.size() <= tokenType.getPrecedence()) {
			tokenDefinitions.add(new ArrayList<ITokenDefinition>());
		}
		tokenDefinitions.get(tokenType.getPrecedence()).add(tokenType);
	}

	/**
	 * Tokenize algorithm. Uses {@code string} as a entry string and returns a
	 * list of Token types.
	 * 
	 * @param string
	 * @return
	 * @throws TokenizationError
	 */
	public List<Token> tokenize(String string) throws TokenizationError {
		List<Token> tokens = new ArrayList<Token>();
		if (string != null && !string.isEmpty()) {
			String tempString = new String(string);
			while (!tempString.isEmpty()) {
				Token currentToken = getNextToken(tempString);
				tokens.add(currentToken);
				// Consume string.
				int sizeToConsume = currentToken.getOriginalString().length();
				if (sizeToConsume <= 0) {
					throw new TokenizationError("Size To consume equals 0 in: '" + tempString + "'");
				}
				tempString = tempString.substring(sizeToConsume);
			}
		}
		return tokens;
	}

	private Token getNextToken(String string) throws TokenizationError {
		for (List<ITokenDefinition> levelTokenDefinitions : tokenDefinitions) {
			Token token = getNextToken(string, levelTokenDefinitions);
			if (token != null) {
				return token;
			}
		}
		throw new TokenizationError(string);
	}

	/**
	 * Get all possible tokenizations with current level of token definitions
	 * 
	 * @param string
	 * @param levelTokenDefinitions
	 * @return
	 */
	private Token getNextToken(String string, List<ITokenDefinition> levelTokenDefinitions) {
		Token token = null;
		for (ITokenDefinition definition : levelTokenDefinitions) {
			Matcher m = definition.getRegexFilterPattern().matcher(string);
			if (m.find() && m.start()==0) {
				// Match found.
				String tokenString = m.group();
				Token tempToken = definition.generateToken(tokenString);
				// If no token yet or larger than current one
				if (token == null || token.getOriginalString().length() < tempToken.getOriginalString().length()) {
					token = tempToken;
				}
			}
		}
		return token;
	}
}
