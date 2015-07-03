package com.biit.webforms.utils.conversor;

import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Conversor of Answer Types
 *
 */
public class ConversorAnswerTypeAbcdToAnswerType implements
		Conversor<AnswerType, com.biit.webforms.enumerations.AnswerType> {

	@Override
	public com.biit.webforms.enumerations.AnswerType convert(AnswerType origin) {
		switch (origin) {
		case INPUT:
			return com.biit.webforms.enumerations.AnswerType.INPUT;
		case MULTI_CHECKBOX:
			return com.biit.webforms.enumerations.AnswerType.MULTIPLE_SELECTION;
		case RADIO:
			return com.biit.webforms.enumerations.AnswerType.SINGLE_SELECTION_RADIO;
		}
		WebformsLogger.severe(this.getClass().getName(), "Unexpected behaviour");
		return null;
	}

}
