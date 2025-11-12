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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.DynamicAnswerNullReference;
import com.biit.webforms.validators.reports.DynamicAnswerReferenceInvalid;
import com.biit.webforms.validators.reports.MultipleDynamicAnswersReferenceTheSameQuestion;

public class ValidateDynamicAnswers extends SimpleValidator<TreeObject> {

	public ValidateDynamicAnswers() {
		super(TreeObject.class);
	}

	@Override
	protected void validateImplementation(TreeObject element) {
		if (element instanceof Question) {
			List<DynamicAnswer> dynamicAnswers = ((Question) element).getChildren(DynamicAnswer.class);
			Set<Question> references = new HashSet<>();
			boolean failed = false;
			for (DynamicAnswer dynamicAnswer : dynamicAnswers) {
				Question currentQuestion = (Question) element;

				assertTrue(dynamicAnswer.getReference() != null, new DynamicAnswerNullReference(currentQuestion));
				if (dynamicAnswer.getReference() != null) {
					assertTrue(currentQuestion.compareTo(dynamicAnswer.getReference()) > 0,
							new DynamicAnswerReferenceInvalid(currentQuestion, dynamicAnswer.getReference()));

					if (references.contains(dynamicAnswer.getReference()) && !failed) {
						failed = true;
						assertFalse(true, new MultipleDynamicAnswersReferenceTheSameQuestion(currentQuestion, dynamicAnswer));
					}
					references.add(dynamicAnswer.getReference());
				}
			}
		} else {
			for (TreeObject child : element.getChildren()) {
				validateImplementation(child);
			}
		}
	}

}
