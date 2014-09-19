package com.biit.webforms.utils.lexer;

import java.util.regex.Pattern;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.lexer.tokens.TokenAnd;
import com.biit.webforms.utils.lexer.tokens.TokenBetween;
import com.biit.webforms.utils.lexer.tokens.TokenDate;
import com.biit.webforms.utils.lexer.tokens.TokenDatePeriod;
import com.biit.webforms.utils.lexer.tokens.TokenEq;
import com.biit.webforms.utils.lexer.tokens.TokenGe;
import com.biit.webforms.utils.lexer.tokens.TokenGt;
import com.biit.webforms.utils.lexer.tokens.TokenLe;
import com.biit.webforms.utils.lexer.tokens.TokenLeftPar;
import com.biit.webforms.utils.lexer.tokens.TokenLt;
import com.biit.webforms.utils.lexer.tokens.TokenNe;
import com.biit.webforms.utils.lexer.tokens.TokenNot;
import com.biit.webforms.utils.lexer.tokens.TokenOr;
import com.biit.webforms.utils.lexer.tokens.TokenReference;
import com.biit.webforms.utils.lexer.tokens.TokenRightPar;
import com.biit.webforms.utils.lexer.tokens.TokenText;
import com.biit.webforms.utils.lexer.tokens.TokenWhitespace;

public enum TokenTypes implements ITokenType {

	WHITESPACE("\\s+|\\n+", 0, TokenWhitespace.class," "),
	// Only for UI consistency.
	RETURN("\\n", 0, TokenWhitespace.class,"\n"),

	LEFT_PAR("\\(", 1, TokenLeftPar.class,"("),

	RIGHT_PAR("\\)", 1, TokenRightPar.class,")"),

	AND("AND|and|&&", 1, TokenAnd.class,"AND"),

	OR("OR|or|\\|\\|", 1, TokenOr.class,"OR"),

	NOT("NOT|not|!", 1, TokenNot.class,"NOT"),

	GT(">", 1, TokenGt.class,">"),

	LT("<", 1, TokenLt.class,"<"),

	GE(">=", 1, TokenGe.class,">="),

	LE("<=", 1, TokenLe.class,"<="),

	EQ("==", 1, TokenEq.class,"=="),

	NE("!=", 1, TokenNe.class,"!="),

	BETWEEN("BETWEEN|between", 1, TokenBetween.class,"BETWEEN"),

	REFERENCE("\\$\\{(<[a-zA-Z][a-zA-Z0-9_.]*>)+\\}", 1, TokenReference.class,"<REFERENCE>"),

	DATE("[0-9][0-9](-|/)[0-9][0-9](-|/)[0-9][0-9][0-9][0-9]", 2, TokenDate.class,"<DATE>"),

	DATE_PERIOD("[0-9]+(Y|M|D)", 2, TokenDatePeriod.class,"<DATE_PERIOD>"),

	TEXT("[a-z|A-Z|0-9][^(\\s)]*", 3, TokenText.class,"<TEXT>"),

	;

	private Pattern regexFilter;
	private int preference;
	private Class<? extends Token> tokenClass;
	private String stringForm;

	TokenTypes(String regexFilter, int precedence, Class<? extends Token> tokenClass,String stringForm) {
		this.regexFilter = Pattern.compile(regexFilter);
		this.preference = precedence;
		this.tokenClass = tokenClass;
		this.stringForm = stringForm;
	}

	@Override
	public String getRegexFilter() {
		return regexFilter.pattern();
	}

	@Override
	public Pattern getRegexFilterPattern() {
		return regexFilter;
	}

	@Override
	public int getLexerPrecedence() {
		return preference;
	}

	@Override
	public Token generateToken(String tokenContent) {
		try {
			Token token = tokenClass.newInstance();
			token.setOriginalString(tokenContent);
			token.setType(this);
			return token;
		} catch (InstantiationException | IllegalAccessException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	@Override
	public boolean match(Token token) {
		return tokenClass.isInstance(token);
	}

	public String getStringForm() {
		return stringForm;
	}
}
