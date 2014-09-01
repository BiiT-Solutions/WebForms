package com.biit.webforms.language;

import com.biit.webforms.persistence.entity.enumerations.AnswerType;

public enum AnswerTypeUi {

	SINGLE_SELECTION_RADIO(AnswerType.SINGLE_SELECTION_RADIO, LanguageCodes.CAPTION_ANSWER_TYPE_SINGLE_SELECT_RADIO),
	
	SINGLE_SELECTION_LIST(AnswerType.SINGLE_SELECTION_LIST, LanguageCodes.CAPTION_ANSWER_TYPE_SINGLE_SELECT_LIST),

	MULTIPLE_SELECTION(AnswerType.MULTIPLE_SELECTION, LanguageCodes.CAPTION_ANSWER_TYPE_MULTI_SELECT),

	// Uses AnswerFormat.
	INPUT(AnswerType.INPUT, LanguageCodes.CAPTION_ANSWER_TYPE_INPUT_FIELD),
	
	TEXT_AREA(AnswerType.TEXT_AREA, LanguageCodes.CAPTION_ANSWER_TYPE_TEXT_AREA);

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
	
	public boolean isUsesAnswerFormat(){
		return answerType.isAnswerFormatEnabled();
	}
}
