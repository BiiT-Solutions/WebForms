package com.biit.webforms.utils.lexer;

import java.util.regex.Pattern;

public interface ITokenType {

	public String getRegexFilter();

	public Pattern getRegexFilterPattern();

	public int getLexerPrecedence();

	public Token generateToken(String tokenContent);

	/**
	 * Matches token type with {@code token} if is an instance of the type, then
	 * returns true. Otherwise false.
	 * 
	 * @param token
	 * @return
	 */
	public boolean match(Token token);
}
