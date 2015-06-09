package com.biit.webforms.validators;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.OthersConditionWithNotMandatoryQuestion;

/**
 * Only allow an 'others' based on mandatory fields
 */
public class ValidateOthersCondition extends SimpleValidator<Flow> {

	public ValidateOthersCondition() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (flow.getOrigin() instanceof Question) {
			if (!((Question) flow.getOrigin()).isMandatory()) {
				assertFalse(flow.isOthers(), new OthersConditionWithNotMandatoryQuestion((Question) flow.getOrigin()));
			}
		}
	}
}