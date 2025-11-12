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
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.vaadin.data.Validator;

import java.util.Objects;

public class ValidatorTablesSameColumnsInEachRow implements Validator {
	private static final long serialVersionUID = -5320539195645056658L;

	private TreeObject treeObject;

	public ValidatorTablesSameColumnsInEachRow(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Boolean isTable = (Boolean) value;
		if (isTable != null && isTable) {
			for (TreeObject group1 : treeObject.getChildren()) {
				for (TreeObject group2 : treeObject.getChildren()) {
					// Different groups
					if (!Objects.equals(group1.getComparationId(), group2.getComparationId())) {
						if (group1.getChildren().size() != group2.getChildren().size()) {
							throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_TABLE_DIFFERENT_NUMBER_OF_COLUMNS.translation());
						}
						for (int i = 0; i < group1.getChildren().size(); i++) {
							try {
								if (group1.getChild(i) instanceof Question && group2.getChild(i) instanceof Question) {
									if (!compare((Question) group1.getChild(i), (Question) group2.getChild(i))) {
										throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_TABLE_DIFFERENT_COLUMNS.translation());
									}
								} else {
									// Already exists another validator for
									// this.
								}
							} catch (ChildrenNotFoundException e) {
								WebformsUiLogger.errorMessage(this.getClass().getName(), e);
							}
						}
					}
				}
			}
		}
	}

	private boolean compare(Question object1, Question object2) {
		return Objects.equals(object1.getName(), object2.getName()) && Objects.equals(object1.isMandatory(), object2.isMandatory())
				&& Objects.equals(object1.getAnswerType(), object2.getAnswerType()) && Objects.equals(object1.getAnswerFormat(), object2.getAnswerFormat())
				&& Objects.equals(object1.getAnswerSubformat(), object2.getAnswerSubformat())
				&& Objects.equals(object1.getDefaultValue(), object2.getDefaultValue());
	}
}
