package com.biit.webforms.validators;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.FlowOriginIsNotMandatory;

public class NoFlowFromOptionalElementValidator extends SimpleValidator<Flow> {

	public NoFlowFromOptionalElementValidator() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		assertTrue(flow.getOrigin() != null
				&& (!(flow.getOrigin() instanceof Question) || ((Question) flow.getOrigin()).isMandatory()),
				new FlowOriginIsNotMandatory(flow));
	}

}
