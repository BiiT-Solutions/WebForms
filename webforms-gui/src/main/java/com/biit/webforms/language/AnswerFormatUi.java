package com.biit.webforms.language;

import com.biit.webforms.persistence.entity.AnswerFormat;

public enum AnswerFormatUi {

	TEXT(AnswerFormat.TEXT, LanguageCodes.CAPTION_ANSWER_FORMAT_TEXT),

	NUMBER(AnswerFormat.NUMBER, LanguageCodes.CAPTION_ANSWER_FORMAT_NUMBER),

	DATE(AnswerFormat.DATE, LanguageCodes.CAPTION_ANSWER_FORMAT_DATE),

	POSTAL_CODE(AnswerFormat.POSTAL_CODE, LanguageCodes.CAPTION_ANSWER_FORMAT_POSTAL_CODE);

	private AnswerFormat answerFormat;
	private LanguageCodes languageCode;

	private AnswerFormatUi(AnswerFormat answerFormat, LanguageCodes languageCode) {
		this.answerFormat = answerFormat;
		this.languageCode = languageCode;
	}

	public AnswerFormat getAnswerFormat() {
		return answerFormat;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}

	public static AnswerFormatUi getFromAnswerFormat(AnswerFormat answerFormat) {
		for (AnswerFormatUi answerFormatUi : values()) {
			if (answerFormatUi.getAnswerFormat().equals(answerFormat)) {
				return answerFormatUi;
			}
		}
		return null;
	}
}
