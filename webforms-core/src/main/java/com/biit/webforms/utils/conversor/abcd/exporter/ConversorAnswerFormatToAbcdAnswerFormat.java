package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.Conversor;

/**
 * Conversor for Answer formats from ABCD to Webforms.
 *
 */
public class ConversorAnswerFormatToAbcdAnswerFormat implements Conversor<com.biit.webforms.enumerations.AnswerFormat, AnswerFormat> {

	@Override
	public AnswerFormat convert(com.biit.webforms.enumerations.AnswerFormat origin) {
		if (origin == null) {
			return null;
		}
		switch (origin) {
		case DATE:
			return AnswerFormat.DATE;
		case NUMBER:
			return AnswerFormat.NUMBER;
		case TEXT:
			return AnswerFormat.TEXT;
		case POSTAL_CODE:
			return AnswerFormat.POSTAL_CODE;
		}
		WebformsLogger.severe(this.getClass().getName(), "Unexpected behaviour");
		return null;
	}

}
