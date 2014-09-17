package com.biit.webforms.utils.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.biit.webforms.utils.lexer.exceptions.TokenizationError;

public class Tokenizer {

	// Token list by precedence.
	private List<List<ITokenDefinition>> tokenDefinitions;

	public Tokenizer() {
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
	public List<IToken> tokenize(String string) throws TokenizationError {
		List<IToken> tokens = new ArrayList<IToken>();
		if (string != null && !string.isEmpty()) {
			String tempString = new String(string);
			while (!tempString.isEmpty()) {
				IToken currentToken = getNextToken(tempString);
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

	private IToken getNextToken(String string) throws TokenizationError {
		for (List<ITokenDefinition> levelTokenDefinitions : tokenDefinitions) {
			IToken token = getNextToken(string, levelTokenDefinitions);
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
	private IToken getNextToken(String string, List<ITokenDefinition> levelTokenDefinitions) {
		IToken token = null;
		for (ITokenDefinition definition : levelTokenDefinitions) {
			Matcher m = definition.getRegexFilterPattern().matcher(string);
			if (m.find() && m.start()==0) {
				// Match found.
				String tokenString = m.group();
				IToken tempToken = definition.generateToken(tokenString);
				// If no token yet or larger than current one
				if (token == null || token.getOriginalString().length() < tempToken.getOriginalString().length()) {
					token = tempToken;
				}
			}
		}
		return token;
	}
}
