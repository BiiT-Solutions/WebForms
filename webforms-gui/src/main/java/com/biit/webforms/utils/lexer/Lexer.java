package com.biit.webforms.utils.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.biit.webforms.utils.lexer.exceptions.StringTokenizationError;
import com.biit.webforms.utils.lexer.exceptions.TokenizationError;

public class Lexer {

	// Token list by precedence.
	private List<List<ITokenType>> tokenDefinitions;

	public Lexer() {
		tokenDefinitions = new ArrayList<List<ITokenType>>();
	}

	public void addTokenDefinition(ITokenType tokenType) {
		// Initialize with empty lists if there is no token with that
		// precendence level
		while (tokenDefinitions.size() <= tokenType.getLexerPrecedence()) {
			tokenDefinitions.add(new ArrayList<ITokenType>());
		}
		tokenDefinitions.get(tokenType.getLexerPrecedence()).add(tokenType);
	}

	/**
	 * Tokenize algorithm. Uses {@code string} as a entry string and returns a
	 * list of Token types.
	 * 
	 * @param string
	 * @return
	 * @throws StringTokenizationError
	 * @throws TokenizationError
	 */
	public List<Token> tokenize(String string) throws StringTokenizationError {
		List<Token> tokens = new ArrayList<Token>();
		if (string != null && !string.isEmpty()) {
			String tempString = new String(string);
			while (!tempString.isEmpty()) {
				try {
					Token currentToken = getNextToken(tempString, string.lastIndexOf(tempString));
					tokens.add(currentToken);
					// Consume string.
					int sizeToConsume = currentToken.getContent().length();
					if (sizeToConsume <= 0) {
						throw new TokenizationError("Size To consume equals 0 in: '" + tempString + "'");
					}
					tempString = tempString.substring(sizeToConsume);
				} catch (TokenizationError e) {
					throw new StringTokenizationError(string, tempString);
				}
			}
		}
		return tokens;
	}

	private Token getNextToken(String string, int originalPosition) throws TokenizationError {
		for (List<ITokenType> levelTokenDefinitions : tokenDefinitions) {
			Token token = getNextToken(string, originalPosition, levelTokenDefinitions);
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
	private Token getNextToken(String string, int originalPosition, List<ITokenType> levelTokenDefinitions) {
		Token token = null;
		for (ITokenType definition : levelTokenDefinitions) {
			Matcher m = definition.getRegexFilterPattern().matcher(string);
			if (m.find() && m.start() == 0) {
				// Match found.
				String tokenString = m.group();
				Token tempToken = definition.generateToken(tokenString, originalPosition);
				// If no token yet or larger than current one
				if (token == null || token.getContent().length() < tempToken.getContent().length()) {
					token = tempToken;
				}
			}
		}
		return token;
	}
}
