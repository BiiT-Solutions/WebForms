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
import com.biit.webforms.persistence.entity.Answer;
import com.vaadin.data.Validator;

public class ValidatorDuplicateValueAnswer implements Validator {
	private static final long serialVersionUID = -5138910904884591785L;

	private Answer answer;

	public ValidatorDuplicateValueAnswer(Answer instance) {
		this.answer = instance;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String newName = (String) value;
		TreeObject parent = answer.getParent();

		if (parent != null) {
			for (TreeObject child : parent.getChildren()) {
				if (child.equals(answer)) {
					// If this child is treeObject, ignore.
					continue;
				}
				if (child.getName().equals(newName)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_ANSWER_VALUE.translation());
				}
			}
		}
	}

}
