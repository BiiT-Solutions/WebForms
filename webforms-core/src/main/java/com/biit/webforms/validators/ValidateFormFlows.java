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
		AllBaseQuestionHaveFlowInValidator allBaseQuestionHaveFlowInValidator = new AllBaseQuestionHaveFlowInValidator();
		if (!allBaseQuestionHaveFlowInValidator.validate(form, getReport())) {
			return;
		}

		ValidateFlow validateFlow = new ValidateFlow();
		if (!validateFlow.validate(form.getFlows(), getReport()) && isStopOnFail()) {
			return;
		}

		OthersOrphanAndUnicityValidator othersUnicityValidator = new OthersOrphanAndUnicityValidator();
		othersUnicityValidator.validate(form, getReport());

		ValidateEmptyFlows validateEmptyFlows = new ValidateEmptyFlows();
		validateEmptyFlows.validate(form, getReport());

		// FlowOriginDestinyUnicityValidator flowOriginDestinyUnicityValidator =
		// new FlowOriginDestinyUnicityValidator();
		// flowOriginDestinyUnicityValidator.validate(form, getReport());
	}

}
