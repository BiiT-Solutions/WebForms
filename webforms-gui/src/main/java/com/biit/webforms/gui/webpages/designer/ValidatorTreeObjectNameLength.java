package com.biit.webforms.gui.webpages.designer;

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

import com.biit.form.entity.TreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Validator;

public class ValidatorTreeObjectNameLength implements Validator {
	private static final long serialVersionUID = 4078897036098602662L;
	private static final int MAX_LENGTH = TreeObject.MAX_UNIQUE_COLUMN_LENGTH;

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (value != null && ((String) value).length() > MAX_LENGTH) {
			throw new InvalidValueException(LanguageCodes.CAPTION_NAME_TOO_LARGE.translation());
		}
	}

}
