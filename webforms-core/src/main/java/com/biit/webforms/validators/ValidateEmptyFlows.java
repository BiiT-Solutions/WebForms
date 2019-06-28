package com.biit.webforms.validators;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;

/**
 * Empty flows can only be defined if no other flows are defined.
 */
public class ValidateEmptyFlows extends SimpleValidator<Form> {

	public ValidateEmptyFlows() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		ComputedFlowView computedFlowView = form.getComputedFlowsView();

		LinkedHashSet<BaseQuestion> baseQuestions = form.getAllChildrenInHierarchy(BaseQuestion.class);

		for (BaseQuestion baseQuestion : baseQuestions) {
			Set<Flow> flows = computedFlowView.getFlowsByOrigin(baseQuestion);
			if (flows != null) {
				for (Flow flow : flows) {
					assertTrue(!(flow.getComputedCondition().isEmpty() && flows.size() > 1), new EmptyFlowIsNotAlone(
							flow));
				}
			}
		}
	}

}
