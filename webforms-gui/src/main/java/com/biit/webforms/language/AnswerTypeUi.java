package com.biit.webforms.language;

import com.biit.webforms.persistence.entity.AnswerType;

public enum AnswerTypeUi {

	RADIO(AnswerType.SINGLE_SELECTION, LanguageCodes.CAPTION_ANSWER_TYPE_RADIO_BUTTON),

	MULTI_CHECKBOX(AnswerType.MULTIPLE_SELECTION, LanguageCodes.CAPTION_ANSWER_TYPE_MULTI_CHECKBOX),

	// Uses AnswerFormat.
	INPUT(AnswerType.INPUT, LanguageCodes.CAPTION_ANSWER_TYPE_INPUT_FIELD);

	private AnswerType answerType;
	private LanguageCodes languageCode;

	private AnswerTypeUi(AnswerType answerType, LanguageCodes languageCode) {
		this.answerType = answerType;
		this.languageCode = languageCode;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}

	public static AnswerTypeUi getFromAnswerType(AnswerType answerType) {
		for (AnswerTypeUi answerTypeUi : values()) {
			if (answerTypeUi.getAnswerType().equals(answerType)) {
				return answerTypeUi;
			}
		}
		return null;
	}
}
