package com.biit.webforms.enumerations;

import java.util.regex.Pattern;

import com.biit.webforms.utils.parser.ITokenType;

public enum TokenTypes implements ITokenType {

	WHITESPACE("\\s+|\\n+", 0, " ", "", "", " "),
	// Only for UI consistency.
	RETURN("\\n", 0, "", "", "", "\u00B6"),

	LEFT_PAR("\\(", 1, "(", "(", "(", "("),

	RIGHT_PAR("\\)", 1, ")", ")", ")", ")"),

	AND("AND|and|&&", 1, "AND", "and", "&", "AND"),

	OR("OR|or|\\|\\|", 1, "OR", "or", "|", "OR"),

	NOT("NOT|not|!", 1, "NOT", "not", "!", "NOT"),

	GT(">", 1, ">", "&gt;", "gt", ">"),

	LT("<", 1, "<", "&lt;", "lt", "<"),

	GE(">=", 1, ">=", "&gt;=", "gt=", ">="),

	LE("<=", 1, "<=", "&lt;=", "lt=", "<="),

	EQ("==", 1, "==", "=", "=", "=="),

	NE("!=", 1, "<>", "!=", "!=", "<>"),

	BETWEEN("BETWEEN|between", 1, "BETWEEN", "between", "between", "BETWEEN"),

	IN("IN|in", 1, "IN", "in", "in", "IN"),
	
	EMPTY("EMPTY|empty", 1, "EMPTY", "empty", "empty", "EMPTY");

	private Pattern regexFilter;
	private int preference;
	private String stringForm;
	private String orbeonRepresentation;
	private String expressionSimplifierRepresentation;
	private String expressionEditorRepressentation;

	TokenTypes(String regexFilter, int precedence, String stringForm, String orbeonRepresentation,
			String expressionSimplifierRepresentation, String expressionEditorRepressentation) {
		this.regexFilter = Pattern.compile(regexFilter);
		this.preference = precedence;
		this.stringForm = stringForm;
		this.orbeonRepresentation = orbeonRepresentation;
		this.expressionSimplifierRepresentation = expressionSimplifierRepresentation;
		this.expressionEditorRepressentation = expressionEditorRepressentation;
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

	public String getExpressionSimplifierRepresentation() {
		return expressionSimplifierRepresentation;
	}

	public static TokenTypes getFromExpressionSimplifierRepresentation(String representation) {
		for (TokenTypes tokenTypes : TokenTypes.values()) {
			if (tokenTypes.getExpressionSimplifierRepresentation().equals(representation)) {
				return tokenTypes;
			}
		}
		return null;
	}

	public String getExpressionEditorRepresentation() {
		return expressionEditorRepressentation;
	}

}
