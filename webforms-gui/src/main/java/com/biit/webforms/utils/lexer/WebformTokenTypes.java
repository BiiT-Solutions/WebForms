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

public enum WebformTokenTypes implements ITokenDefinition {

	WHITESPACE("\\s+|\\n+", 0, TokenWhitespace.class),

	LEFT_PAR("\\(", 1, TokenLeftPar.class),

	RIGHT_PAR("\\)", 1, TokenRightPar.class),

	AND("AND|and|&&", 1, TokenAnd.class),

	OR("OR|or|\\|\\|", 1, TokenOr.class),

	NOT("NOT|not|!", 1, TokenNot.class),

	GT(">", 1, TokenGt.class),

	LT("<", 1, TokenLt.class),

	GE(">=", 1, TokenGe.class),

	LE("<=", 1, TokenLe.class),

	EQ("==", 1, TokenEq.class),

	NE("!=", 1, TokenNe.class),

	BETWEEN("BETWEEN|between", 1, TokenBetween.class),

	REFERENCE("\\$\\{[a-z|A-z|_|.]+\\}", 1, TokenReference.class),

	DATE("[0-9][0-9](-|/)[0-9][0-9](-|/)[0-9][0-9][0-9][0-9]", 2, TokenDate.class),

	DATE_PERIOD("[0-9]+(Y|M|D)", 2, TokenDatePeriod.class),

	TEXT("[a-z|A-Z|0-9][^(\\s)]*", 3, TokenText.class);

	private Pattern regexFilter;
	private int preference;
	private Class<? extends IToken> classToGenerate;

	WebformTokenTypes(String regexFilter, int precedence, Class<? extends IToken> classToGenerate) {
		this.regexFilter = Pattern.compile(regexFilter);
		this.preference = precedence;
		this.classToGenerate = classToGenerate;
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
	public int getPrecedence() {
		return preference;
	}

	@Override
	public IToken generateToken(String tokenContent) {
		try {
			IToken token = classToGenerate.newInstance();
			token.setOriginalString(tokenContent);
			return token;
		} catch (InstantiationException | IllegalAccessException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

}
