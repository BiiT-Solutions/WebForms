package com.biit.webforms.enumerations;

import java.util.regex.Pattern;

import com.biit.webforms.utils.parser.ITokenType;

public enum TokenTypes implements ITokenType {

	WHITESPACE("\\s+|\\n+", 0, " ", ""),
	// Only for UI consistency.
	RETURN("\\n", 0, "\n", ""),

	LEFT_PAR("\\(", 1, "(", "("),

	RIGHT_PAR("\\)", 1, ")", ")"),

	AND("AND|and|&&", 1, "AND", "and"),

	OR("OR|or|\\|\\|", 1, "OR", "or"),

	NOT("NOT|not|!", 1, "NOT", "not"),

	GT(">", 1, ">", "&gt;"),

	LT("<", 1, "<", "&lt;"),

	GE(">=", 1, ">=", "&gt;="),

	LE("<=", 1, "<=", "&lt;="),

	EQ("==", 1, "==", "="),

	NE("!=", 1, "!=", "!="),

	BETWEEN("BETWEEN|between", 1, "BETWEEN", "between"),

	;

	private Pattern regexFilter;
	private int preference;
	private String stringForm;
	private String orbeonRepresentation;

	TokenTypes(String regexFilter, int precedence, String stringForm, String orbeonRepresentation) {
		this.regexFilter = Pattern.compile(regexFilter);
		this.preference = precedence;
		this.stringForm = stringForm;
		this.orbeonRepresentation = orbeonRepresentation;
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

	@Deprecated
	public String getStringForm() {
		return stringForm;
	}

	@Override
	public String toString() {
		return stringForm;
	}

	public static TokenTypes fromString(String string) {
		for (TokenTypes value : values()) {
			if (value.stringForm.equals(string)) {
				return value;
			}
		}
		return null;
	}

	public String getOrbeonRepresentation() {
		return orbeonRepresentation;
	}
}
