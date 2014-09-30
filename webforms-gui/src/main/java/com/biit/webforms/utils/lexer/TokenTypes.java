package com.biit.webforms.utils.lexer;

import java.util.regex.Pattern;

import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.lexer.tokens.TokenAnd;
import com.biit.webforms.utils.lexer.tokens.TokenPostalCode;
import com.biit.webforms.utils.lexer.tokens.TokenValueAnswer;
import com.biit.webforms.utils.lexer.tokens.TokenBetween;
import com.biit.webforms.utils.lexer.tokens.TokenValueDate;
import com.biit.webforms.utils.lexer.tokens.TokenValueDatePeriod;
import com.biit.webforms.utils.lexer.tokens.TokenEq;
import com.biit.webforms.utils.lexer.tokens.TokenValueFloat;
import com.biit.webforms.utils.lexer.tokens.TokenGe;
import com.biit.webforms.utils.lexer.tokens.TokenGt;
import com.biit.webforms.utils.lexer.tokens.TokenLe;
import com.biit.webforms.utils.lexer.tokens.TokenLeftPar;
import com.biit.webforms.utils.lexer.tokens.TokenLt;
import com.biit.webforms.utils.lexer.tokens.TokenNe;
import com.biit.webforms.utils.lexer.tokens.TokenNot;
import com.biit.webforms.utils.lexer.tokens.TokenValueNumber;
import com.biit.webforms.utils.lexer.tokens.TokenOr;
import com.biit.webforms.utils.lexer.tokens.TokenReference;
import com.biit.webforms.utils.lexer.tokens.TokenRightPar;
import com.biit.webforms.utils.lexer.tokens.TokenValueText;
import com.biit.webforms.utils.lexer.tokens.TokenWhitespace;

public enum TokenTypes implements ITokenType {

	WHITESPACE("\\s+|\\n+", 0, TokenWhitespace.class, " "),
	// Only for UI consistency.
	RETURN("\\n", 0, TokenWhitespace.class, "\n"),

	LEFT_PAR("\\(", 1, TokenLeftPar.class, "("),

	RIGHT_PAR("\\)", 1, TokenRightPar.class, ")"),

	AND("AND|and|&&", 1, TokenAnd.class, "AND"),

	OR("OR|or|\\|\\|", 1, TokenOr.class, "OR"),

	NOT("NOT|not|!", 1, TokenNot.class, "NOT"),

	GT(">", 1, TokenGt.class, ">"),

	LT("<", 1, TokenLt.class, "<"),

	GE(">=", 1, TokenGe.class, ">="),

	LE("<=", 1, TokenLe.class, "<="),

	EQ("==", 1, TokenEq.class, "=="),

	NE("!=", 1, TokenNe.class, "!="),

	BETWEEN("BETWEEN|between", 1, TokenBetween.class, "BETWEEN"),

	REFERENCE("<" + TreeObject.XML_TAG_ALLOWED_CHARS + ">", 1, TokenReference.class, "<REFERENCE>"),

	VALUE_ANSWER("%" + BaseAnswer.ANSWER_TAG_ALLOWED_CHARS + "%", 1, TokenValueAnswer.class, "%ANSWER%"),

	VALUE_TEXT("\"[^\\\\\"]*(\\\\\")*[^\\\\\"]*\"", 2, TokenValueText.class, "\"TEXT\""),

	VALUE_DATE(WebformsConfigurationReader.getInstance().getRegexDate(), 2, TokenValueDate.class, "DATE"),

	VALUE_DATE_PERIOD(WebformsConfigurationReader.getInstance().getRegexDatePeriod(), 2, TokenValueDatePeriod.class, "DATE_PERIOD"),

	VALUE_NUMBER(WebformsConfigurationReader.getInstance().getRegexNumber(), 2, TokenValueNumber.class, "NUMBER"),

	VALUE_FLOAT(WebformsConfigurationReader.getInstance().getRegexFloat(), 2, TokenValueFloat.class, "FLOAT"),

	VALUE_POSTAL_CODE(WebformsConfigurationReader.getInstance().getRegexPostalCode(), 2, TokenPostalCode.class, "POSTAL_CODE"),
	
	;

	private Pattern regexFilter;
	private int preference;
	private Class<? extends Token> tokenClass;
	private String stringForm;

	TokenTypes(String regexFilter, int precedence, Class<? extends Token> tokenClass, String stringForm) {
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
	public Token generateToken(String tokenContent, int originalPosition) {
		try {
			Token token = tokenClass.newInstance();
			token.setOriginalString(tokenContent, originalPosition);
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
