package com.biit.webforms.validators;

import com.biit.form.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.validators.reports.BackwardFlow;

public class NoFlowBackwardValidator extends SimpleValidator<Flow> {

	public NoFlowBackwardValidator() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		TreeObject origin = flow.getOrigin();
		TreeObject destiny = flow.getDestiny();
		if (origin != null && destiny != null) {
			assertTrue(origin.compareTo(destiny) < 0, new BackwardFlow(flow));
		}
	}
}
