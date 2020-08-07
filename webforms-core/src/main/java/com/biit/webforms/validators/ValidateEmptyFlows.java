package com.biit.webforms.validators;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;

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
					assertTrue(!(isEmptyFlow(flow) && flows.size() > 1), new EmptyFlowIsNotAlone(flow));
				}
			}
		}
	}

	private boolean isEmptyFlow(Flow flow) {
		// Token empty can be used with others flows. Is an exception.
		for (Token token : flow.getComputedCondition()) {
			if (Objects.deepEquals(token.getType(), TokenTypes.EMPTY)) {
				return false;
			}
		}
		return flow.getComputedCondition().isEmpty();
	}

}
