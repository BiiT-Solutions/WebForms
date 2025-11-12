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

import com.biit.webforms.enumerations.AnswerType;

public enum AnswerTypeUi {

	SINGLE_SELECTION_RADIO(AnswerType.SINGLE_SELECTION_RADIO, LanguageCodes.CAPTION_ANSWER_TYPE_SINGLE_SELECT_RADIO),
	
	SINGLE_SELECTION_LIST(AnswerType.SINGLE_SELECTION_LIST, LanguageCodes.CAPTION_ANSWER_TYPE_SINGLE_SELECT_LIST),

	MULTIPLE_SELECTION(AnswerType.MULTIPLE_SELECTION, LanguageCodes.CAPTION_ANSWER_TYPE_MULTI_SELECT),
	
	SLIDER(AnswerType.SINGLE_SELECTION_SLIDER, LanguageCodes.CAPTION_ANSWER_TYPE_SLIDER),

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
