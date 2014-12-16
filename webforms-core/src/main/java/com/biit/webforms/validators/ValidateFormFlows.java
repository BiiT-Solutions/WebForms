package com.biit.webforms.validators;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Form;

/**
 * Validates all the conditions for the form flow.
 * 
 */
public class ValidateFormFlows extends SimpleValidator<Form> {

	public ValidateFormFlows() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {

		ValidateFlow validateFlow = new ValidateFlow();
		if (!validateFlow.validate(form.getFlows(), getReport()) && isStopOnFail()) {
			return;
		}
		OthersUnicityValidator othersUnicityValidator = new OthersUnicityValidator();
		othersUnicityValidator.validate(form, getReport());
//		FlowOriginDestinyUnicityValidator flowOriginDestinyUnicityValidator = new FlowOriginDestinyUnicityValidator();
//		flowOriginDestinyUnicityValidator.validate(form, getReport());
	}
	
}
