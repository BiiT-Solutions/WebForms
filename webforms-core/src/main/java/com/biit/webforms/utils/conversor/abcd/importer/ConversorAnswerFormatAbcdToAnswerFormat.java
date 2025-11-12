package com.biit.webforms.utils.conversor.abcd.importer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Conversor for Answer formats from ABCD to Webforms.
 *
 */
public class ConversorAnswerFormatAbcdToAnswerFormat implements
		Conversor<AnswerFormat, com.biit.webforms.enumerations.AnswerFormat> {

	@Override
	public com.biit.webforms.enumerations.AnswerFormat convert(AnswerFormat origin) {
		if(origin == null){
			return null;
		}
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
