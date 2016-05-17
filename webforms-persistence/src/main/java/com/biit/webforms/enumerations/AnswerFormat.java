package com.biit.webforms.enumerations;

/**
 * Used only for text inputs.
 */
public enum AnswerFormat {

	TEXT(AnswerSubformat.TEXT, new AnswerSubformat[] { AnswerSubformat.TEXT, AnswerSubformat.EMAIL, AnswerSubformat.PHONE,
			AnswerSubformat.IBAN, AnswerSubformat.BSN }, new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE }),

	NUMBER(AnswerSubformat.NUMBER, new AnswerSubformat[] { AnswerSubformat.NUMBER, AnswerSubformat.FLOAT, AnswerSubformat.POSITIVE_NUMBER,
			AnswerSubformat.NEGATIVE_NUMBER, AnswerSubformat.POSITIVE_FLOAT, AnswerSubformat.NEGATIVE_FLOAT }, new TokenTypes[] {
			TokenTypes.EQ, TokenTypes.NE, TokenTypes.GT, TokenTypes.LT, TokenTypes.GE, TokenTypes.LE, TokenTypes.BETWEEN, TokenTypes.IN }),

	DATE(AnswerSubformat.DATE, new AnswerSubformat[] { AnswerSubformat.DATE, AnswerSubformat.DATE_PAST, AnswerSubformat.DATE_FUTURE,
			AnswerSubformat.DATE_PERIOD, AnswerSubformat.DATE_BIRTHDAY }, new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE, TokenTypes.GT,
			TokenTypes.LT, TokenTypes.GE, TokenTypes.LE, TokenTypes.BETWEEN }),

	POSTAL_CODE(AnswerSubformat.POSTAL_CODE, new AnswerSubformat[] { AnswerSubformat.POSTAL_CODE }, new TokenTypes[] { TokenTypes.EQ,
			TokenTypes.NE, TokenTypes.GT, TokenTypes.LT, TokenTypes.GE, TokenTypes.LE, TokenTypes.BETWEEN, TokenTypes.IN })

	;

	private final AnswerSubformat defaultSubformat;
	private final AnswerSubformat[] subformats;
	private final TokenTypes[] validTokenTypes;

	private AnswerFormat(AnswerSubformat defaultSubformat, AnswerSubformat[] subformats, TokenTypes[] validTokenTypes) {
		this.defaultSubformat = defaultSubformat;
		this.subformats = subformats;
		this.validTokenTypes = validTokenTypes;
	}

	public AnswerSubformat[] getSubformats() {
		return subformats;
	}

	public AnswerSubformat getDefaultSubformat() {
		return defaultSubformat;
	}

	public boolean isSubformat(AnswerSubformat subformat) {
		for (AnswerSubformat value : subformats) {
			if (value.equals(subformat)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidTokenType(TokenTypes tokenType) {
		for (int i = 0; i < validTokenTypes.length; i++) {
			if (validTokenTypes[i].equals(tokenType)) {
				return true;
			}
		}
		return false;
	}

	public TokenTypes[] getValidTokenTypes() {
		return validTokenTypes;
	}
}
