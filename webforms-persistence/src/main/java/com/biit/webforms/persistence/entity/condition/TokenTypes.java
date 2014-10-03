package com.biit.webforms.persistence.entity.condition;

import java.util.regex.Pattern;

import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.webforms.configuration.WebformsConfigurationReader;

public enum TokenTypes implements ITokenType {

	WHITESPACE("\\s+|\\n+", 0, " "),
	// Only for UI consistency.
	RETURN("\\n", 0, "\n"),

	LEFT_PAR("\\(", 1, "("),

	RIGHT_PAR("\\)", 1, ")"),

	AND("AND|and|&&", 1, "AND"),

	OR("OR|or|\\|\\|", 1,"OR"),

	NOT("NOT|not|!", 1,"NOT"),

	GT(">", 1,">"),

	LT("<", 1, "<"),

	GE(">=", 1,">="),

	LE("<=", 1,"<="),

	EQ("==", 1,"=="),

	NE("!=", 1,"!="),

	BETWEEN("BETWEEN|between", 1,"BETWEEN"),

	REFERENCE("<" + TreeObject.XML_TAG_ALLOWED_CHARS + ">", 1,"<REFERENCE>"),

	REFERENCE_ANSWER("'" + BaseAnswer.ANSWER_TAG_ALLOWED_CHARS + "'", 1, "'ANSWER'"),

	VALUE_TEXT("\"[^\\\\\"]*(\\\\\")*[^\\\\\"]*\"", 2, "\"TEXT\""),

	VALUE_DATE(WebformsConfigurationReader.getInstance().getRegexDate(), 2, "DATE"),

	VALUE_DATE_PERIOD(WebformsConfigurationReader.getInstance().getRegexDatePeriod(), 2,"DATE_PERIOD"),

	VALUE_NUMBER(WebformsConfigurationReader.getInstance().getRegexNumber(), 2, "NUMBER"),

	VALUE_FLOAT(WebformsConfigurationReader.getInstance().getRegexFloat(), 2,"FLOAT"),

	VALUE_POSTAL_CODE(WebformsConfigurationReader.getInstance().getRegexPostalCode(), 2, "POSTAL_CODE"),
	
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
	public String toString(){
		return stringForm;
	}
}
