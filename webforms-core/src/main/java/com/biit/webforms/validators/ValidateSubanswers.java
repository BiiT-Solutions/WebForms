package com.biit.webforms.validators;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.NoSubanswersAllowed;

/**
 * Subanswers can only be used in RadioButtons.
 */
public class ValidateSubanswers extends SimpleValidator<TreeObject> {

	public ValidateSubanswers() {
		super(TreeObject.class);
	}

	@Override
	protected void validateImplementation(TreeObject element) {
		if (element instanceof BaseAnswer) {
			if (!element.getChildren().isEmpty()) {
				Question parent = ((Question) element.getParent());
				assertTrue(parent.getAnswerType().equals(AnswerType.SINGLE_SELECTION_RADIO)
						|| parent.getAnswerType().equals(AnswerType.MULTIPLE_SELECTION),
						new NoSubanswersAllowed(parent));
			}
		} else {
			for (TreeObject child : element.getChildren()) {
				validateImplementation(child);
			}
		}
	}
}
