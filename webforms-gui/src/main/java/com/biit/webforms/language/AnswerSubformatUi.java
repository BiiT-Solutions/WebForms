package com.biit.webforms.language;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;

public enum AnswerSubformatUi {

	TEXT(AnswerSubformat.TEXT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_TEXT),

	EMAIL(AnswerSubformat.EMAIL, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_EMAIL),

	PHONE(AnswerSubformat.PHONE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_PHONE),

	IBAN(AnswerSubformat.IBAN, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_IBAN),

	BSN(AnswerSubformat.BSN, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_BSN),

	NUMBER(AnswerSubformat.NUMBER, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_NUMBER),

	FLOAT(AnswerSubformat.FLOAT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_FLOAT),

	DATE(AnswerSubformat.DATE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE),

	DATE_PAST(AnswerSubformat.DATE_PAST, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE_PAST),

	DATE_FUTURE(AnswerSubformat.DATE_FUTURE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE_FUTURE),

	POSTAL_CODE(AnswerSubformat.POSTAL_CODE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_POSTAL_CODE),
	
	//Date period is only an internal unit and is not used in Ui.
	//	DATE_PERIOD(AnswerSubformat.DATE_PERIOD, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE_PERIOD),
	
	;

	private final AnswerSubformat subformat;
	private final LanguageCodes translationCode;

	private AnswerSubformatUi(AnswerSubformat subformat, LanguageCodes translationCode) {
		this.subformat = subformat;
		this.translationCode = translationCode;
	}

	public AnswerSubformat getSubformat() {
		return subformat;
	}

	public LanguageCodes getTranslationCode() {
		return translationCode;
	}

	public static AnswerSubformatUi get(AnswerSubformat subformat) {
		for (AnswerSubformatUi value : values()) {
			if (value.subformat.equals(subformat)) {
				return value;
			}
		}
		return null;
	}

	public static AnswerSubformatUi[] values(AnswerFormat format) {
		List<AnswerSubformatUi> values = new ArrayList<>();
		if (format != null) {
			AnswerSubformat subformats[] = format.getSubformats();
			for (AnswerSubformat subformat : subformats) {
				AnswerSubformatUi subformatUi = get(subformat);
				if (subformatUi != null) {
					values.add(subformatUi);
				}
			}
		}
		return values.toArray(new AnswerSubformatUi[] {});
	}
}
