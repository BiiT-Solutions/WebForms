package com.biit.webforms.language;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;

import java.util.ArrayList;
import java.util.List;

public enum AnswerSubformatUi {

	// AMOUNT(AnswerSubformat.AMOUNT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_AMOUNT, LanguageCodes.INPUT_PROMPT_AMOUNT),

	TEXT(AnswerSubformat.TEXT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_TEXT, LanguageCodes.INPUT_PROMPT_TEXT),

	EMAIL(AnswerSubformat.EMAIL, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_EMAIL, LanguageCodes.INPUT_PROMPT_EMAIL),

	PHONE(AnswerSubformat.PHONE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_PHONE, LanguageCodes.INPUT_PROMPT_PHONE),

	IBAN(AnswerSubformat.IBAN, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_IBAN, LanguageCodes.INPUT_PROMPT_IBAN),

	BSN(AnswerSubformat.BSN, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_BSN, LanguageCodes.INPUT_PROMPT_BSN),

	NUMBER(AnswerSubformat.NUMBER, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_NUMBER, LanguageCodes.INPUT_PROMPT_NUMBER),
	
	POSITIVE_NUMBER(AnswerSubformat.POSITIVE_NUMBER, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_POSITIVE_NUMBER, LanguageCodes.INPUT_PROMPT_POSITIVE_NUMBER),
	
	NEGATIVE_NUMBER(AnswerSubformat.NEGATIVE_NUMBER, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_NEGATIVE_NUMBER, LanguageCodes.INPUT_PROMPT_NEGATIVE_NUMBER),

	FLOAT(AnswerSubformat.FLOAT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_FLOAT, LanguageCodes.INPUT_PROMPT_FLOAT),
	
	POSITIVE_FLOAT(AnswerSubformat.POSITIVE_FLOAT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_POSITIVE_FLOAT, LanguageCodes.INPUT_PROMPT_POSITIVE_FLOAT),
	
	NEGATIVE_FLOAT(AnswerSubformat.NEGATIVE_FLOAT, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_NEGATIVE_FLOAT, LanguageCodes.INPUT_PROMPT_NEGATIVE_FLOAT),

	DATE(AnswerSubformat.DATE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE, LanguageCodes.INPUT_PROMPT_DATE),
	
	DATE_PERIOD(AnswerSubformat.DATE_PERIOD, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE, LanguageCodes.INPUT_PROMPT_DATE_PERIOD),

	DATE_PAST(AnswerSubformat.DATE_PAST, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE_PAST,
			LanguageCodes.INPUT_PROMPT_DATE),

	DATE_FUTURE(AnswerSubformat.DATE_FUTURE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE_FUTURE,
			LanguageCodes.INPUT_PROMPT_DATE),

	POSTAL_CODE(AnswerSubformat.POSTAL_CODE, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_POSTAL_CODE,
			LanguageCodes.INPUT_PROMPT_POSTAL_CODE),

	// Date period is only an internal unit and is not used in Ui.
	// DATE_PERIOD(AnswerSubformat.DATE_PERIOD, LanguageCodes.CAPTION_ANSWER_SUBFORMAT_DATE_PERIOD),

	;

	private final AnswerSubformat subformat;
	private final LanguageCodes languageCode;
	private final LanguageCodes inputprompt;

	private AnswerSubformatUi(AnswerSubformat subformat, LanguageCodes translationCode, LanguageCodes inputprompt) {
		this.subformat = subformat;
		this.languageCode = translationCode;
		this.inputprompt = inputprompt;
	}

	public AnswerSubformat getSubformat() {
		return subformat;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
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

	public String getInputPrompt() {
		return inputprompt.translation();
	}
}
