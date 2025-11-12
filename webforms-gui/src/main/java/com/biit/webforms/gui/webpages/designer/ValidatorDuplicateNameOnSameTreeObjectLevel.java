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
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Validator;

public class ValidatorDuplicateNameOnSameTreeObjectLevel implements Validator {
	private static final long serialVersionUID = -6253757334392693487L;

	private TreeObject treeObject;

	public ValidatorDuplicateNameOnSameTreeObjectLevel(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String newName = (String) value;
		TreeObject parent = treeObject.getParent();

		if (parent != null) {
			validateNotDuplicatedName(parent, newName);
			// Checks also the form reference.
			if (parent instanceof Form) {
				if (((Form) parent).getFormReference() != null) {
					validateNotDuplicatedName(((Form) parent).getFormReference(), newName);
				}
			}

		}
	}

	private void validateNotDuplicatedName(TreeObject parent, String newName) throws InvalidValueException {
		if (parent != null) {
			for (TreeObject child : parent.getChildren()) {
				if (child.equals(treeObject) || child instanceof BlockReference) {
					// If this child is treeObject, ignore.
					continue;
				}
				if (child.getName().equals(newName)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME.translation());
				}
				if (child.getName().equalsIgnoreCase(newName)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME_CASE.translation());
				}
			}
		}
	}

}
