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
