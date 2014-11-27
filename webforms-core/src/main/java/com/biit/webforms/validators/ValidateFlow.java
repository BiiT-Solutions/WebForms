package com.biit.webforms.validators;

import com.biit.utils.validation.CompositeValidator;
import com.biit.webforms.persistence.entity.Flow;

public class ValidateFlow extends CompositeValidator<Flow> {

	public ValidateFlow() {
		super(Flow.class);
		add(NoFlowBackwardValidator.class);
		add(NoFlowFromOptionalElementValidator.class);
		add(AllQuestionAnswerValueUseFinalAnswer.class);
		add(ValidateFlowCondition.class);
		add(ValidateFlowSubformat.class);
	}

}
