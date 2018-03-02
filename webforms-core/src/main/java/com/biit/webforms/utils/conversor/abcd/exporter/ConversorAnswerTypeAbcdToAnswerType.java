package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.Conversor;

/**
 * Conversor of Answer Types
 *
 */
public class ConversorAnswerTypeAbcdToAnswerType implements Conversor<com.biit.webforms.enumerations.AnswerType, AnswerType> {

	@Override
	public AnswerType convert(com.biit.webforms.enumerations.AnswerType origin) {
		switch (origin) {
		case INPUT:
		case TEXT_AREA:
			return AnswerType.INPUT;
		case MULTIPLE_SELECTION:
			return AnswerType.MULTI_CHECKBOX;
		case SINGLE_SELECTION_RADIO:
		case SINGLE_SELECTION_LIST:
		case SINGLE_SELECTION_SLIDER:
			return AnswerType.RADIO;
		}
		WebformsLogger.severe(this.getClass().getName(), "Unexpected behaviour");
		return null;
	}

}
