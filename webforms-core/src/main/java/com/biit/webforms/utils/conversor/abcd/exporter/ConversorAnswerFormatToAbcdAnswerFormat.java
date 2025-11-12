package com.biit.webforms.utils.conversor.abcd.exporter;

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
