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

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Conversor from Abcd Form to Webforms form.
 *
 * Both forms only have categories as childs.
 *
 */
public class ConversorAbcdFormToForm extends ConversorTreeObject<Form, com.biit.webforms.persistence.entity.Form> {

	private ConversorAbcdCategoryToCategory conversorCategory = new ConversorAbcdCategoryToCategory();

	@Override
	public com.biit.webforms.persistence.entity.Form createDestinyInstance() {
		return new com.biit.webforms.persistence.entity.Form();
	}

	@Override
	public void copyData(Form origin, com.biit.webforms.persistence.entity.Form destiny) {
		// Copy base data
		try {
			destiny.setLabel(origin.getLabel());
		} catch (FieldTooLongException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		// Create copy of the childs and assign.
		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof Category)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			com.biit.webforms.persistence.entity.Category convertedChild = conversorCategory.convert((Category) child);
			try {
				destiny.addChild(convertedChild);
			} catch (NotValidChildException | ElementIsReadOnly e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}

		}
	}
}
