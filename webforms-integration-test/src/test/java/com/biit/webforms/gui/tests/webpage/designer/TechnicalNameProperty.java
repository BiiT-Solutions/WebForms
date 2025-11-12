package com.biit.webforms.gui.tests.webpage.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Test)
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

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

public class TechnicalNameProperty extends CommonTreeElementProperties {

	private static final String TECHNICAL_NAME_FIELD_CAPTION = "Technical Name";

	public String getTechnicalName() {
		return getTextFieldValue(TECHNICAL_NAME_FIELD_CAPTION);
	}

	public void setTechnicalName(String value) throws FieldNotEditableException {
		setTextFieldValue(TECHNICAL_NAME_FIELD_CAPTION, value);
	}
}
