package com.biit.webforms.enumerations;

import java.util.regex.Pattern;

import com.biit.webforms.utils.parser.ITokenType;

public enum TokenTypes implements ITokenType {

	WHITESPACE("\\s+|\\n+", 0, " "),
	// Only for UI consistency.
	RETURN("\\n", 0, "\n"),

	LEFT_PAR("\\(", 1, "("),

	RIGHT_PAR("\\)", 1, ")"),

	AND("AND|and|&&", 1, "AND"),

	OR("OR|or|\\|\\|", 1, "OR"),

	NOT("NOT|not|!", 1, "NOT"),

	GT(">", 1, ">"),

	LT("<", 1, "<"),

	GE(">=", 1, ">="),

	LE("<=", 1, "<="),

	EQ("==", 1, "=="),

	NE("!=", 1, "!="),

	BETWEEN("BETWEEN|between", 1, "BETWEEN"),

	;

	private Pattern regexFilter;
	private int preference;
	private String stringForm;

	TokenTypes(String regexFilter, int precedence, String stringForm) {
		this.regexFilter = Pattern.compile(regexFilter);
		this.preference = precedence;
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

	@Deprecated
	public String getStringForm() {
		return stringForm;
	}

	@Override
	public String toString() {
		return stringForm;
	}
	
	public static TokenTypes fromString(String string){
		for(TokenTypes value: values()){
			if(value.stringForm.equals(string)){
				return value;
			}
		}
		return null;
	}
}
