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
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.vaadin.data.Validator;

public class ValidatorNestedTablesNotAllowed implements Validator {
	private static final long serialVersionUID = -5320539195645056658L;

	private TreeObject treeObject;

	public ValidatorNestedTablesNotAllowed(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Boolean isTable = (Boolean) value;
		if (isTable != null && isTable) {
			TreeObject parent = treeObject.getParent();

			if (parent != null) {
				validateNotNestedTables(parent, isTable);
			}
		}
	}

	private void validateNotNestedTables(TreeObject parent, Boolean isTable) throws InvalidValueException {
		if (parent != null) {
			if (parent instanceof Group) {
				if (((Group) parent).isShownAsTable() && isTable) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_NESTED_TABLE.translation());
				}
			} else {
				// Checks also the form reference.
				if (parent instanceof Form) {
					if (((Form) parent).getFormReference() != null) {
						validateNotNestedTables(((Form) parent).getFormReference(), isTable);
					}
				}
			}
			validateNotNestedTables(parent.getParent(), isTable);
		}
	}

}
