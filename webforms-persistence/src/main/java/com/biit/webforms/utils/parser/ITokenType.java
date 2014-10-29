package com.biit.webforms.utils.parser;

import java.util.regex.Pattern;

public interface ITokenType {

	public String getRegexFilter();

	public Pattern getRegexFilterPattern();

	public int getLexerPrecedence();
}
