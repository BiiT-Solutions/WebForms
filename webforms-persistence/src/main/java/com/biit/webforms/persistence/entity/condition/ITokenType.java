package com.biit.webforms.persistence.entity.condition;

import java.util.regex.Pattern;

public interface ITokenType {

	public String getRegexFilter();

	public Pattern getRegexFilterPattern();

	public int getLexerPrecedence();
}
