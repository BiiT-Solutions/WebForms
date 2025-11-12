package com.biit.webforms.validators;

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

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.InvalidDefaultValue;

public class ValidateDefaultValues extends SimpleValidator<TreeObject> {

	public ValidateDefaultValues() {
		super(TreeObject.class);
	}

	@Override
	protected void validateImplementation(TreeObject element) {
		for (TreeObject child : element.getAllChildrenInHierarchy(Question.class)) {
			Question question = ((Question) child);
			if (question.getDefaultValueString() != null && question.getAnswerFormat() != null) {
				if (question.getAnswerFormat().equals(AnswerFormat.NUMBER)) {
					if (question.getAnswerSubformat().equals(AnswerSubformat.NUMBER)) {
						assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexNumber()),
								new InvalidDefaultValue(question));
					} else if (question.getAnswerSubformat().equals(AnswerSubformat.FLOAT)) {
						assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexFloat()),
								new InvalidDefaultValue(question));
					}
				} else if (question.getAnswerFormat().equals(AnswerFormat.DATE)) {
					assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexDate()), new InvalidDefaultValue(
							question));
				} else if (question.getAnswerFormat().equals(AnswerFormat.POSTAL_CODE)) {
					assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexPostalCode()),
							new InvalidDefaultValue(question));
				}
			}
		}
	}
}
