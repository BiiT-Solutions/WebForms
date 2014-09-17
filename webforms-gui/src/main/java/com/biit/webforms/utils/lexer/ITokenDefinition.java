package com.biit.webforms.utils.lexer;

import java.util.regex.Pattern;

public interface ITokenDefinition {

	public String getRegexFilter();
	public Pattern getRegexFilterPattern();
	public int getPrecedence();
	public Token generateToken(String tokenContent);
	
}
