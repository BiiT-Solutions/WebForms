package com.biit.webforms.validators;

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
