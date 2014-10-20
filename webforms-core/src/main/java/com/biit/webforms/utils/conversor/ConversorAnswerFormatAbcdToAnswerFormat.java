package com.biit.webforms.utils.conversor;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.webforms.logger.WebformsLogger;

public class ConversorAnswerFormatAbcdToAnswerFormat implements
		Conversor<AnswerFormat, com.biit.webforms.enumerations.AnswerFormat> {

	@Override
	public com.biit.webforms.enumerations.AnswerFormat convert(AnswerFormat origin) {
		switch (origin) {
		case DATE:
			return com.biit.webforms.enumerations.AnswerFormat.DATE;
		case NUMBER:
			return com.biit.webforms.enumerations.AnswerFormat.NUMBER;
		case TEXT:
			return com.biit.webforms.enumerations.AnswerFormat.TEXT;
		case POSTAL_CODE:
			return com.biit.webforms.enumerations.AnswerFormat.POSTAL_CODE;
		}
		WebformsLogger.severe(this.getClass().getName(), "Unexpected behaviour");
		return null;
	}

}
