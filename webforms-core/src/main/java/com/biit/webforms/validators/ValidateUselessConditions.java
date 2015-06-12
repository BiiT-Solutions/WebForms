package com.biit.webforms.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;

/**
 * Redundant conditions are checked with this validator. "A -- A=1 --> B --> C -- A=1 --> D" In this
 * example," C -- A=1 --> D" is useless and can cause problems if it also has an 'OTHER' rule due to the 'OTHER' will be
 * translated as NOT(A=1) and therefore D will be shown if A=2 but C not.
 */
public class ValidateUselessConditions extends SimpleValidator<Flow> {
	private HashMap<TreeObject, Integer> questionIndex;
	private ComputedFlowView computedFlowView;

	public ValidateUselessConditions() {
		super(Flow.class);
		questionIndex = null;
		computedFlowView = null;
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (!flow.getCondition().isEmpty() && !flow.isOthers()) {
			for (Token token : flow.getCondition()) {
				BaseQuestion question = null;
				// Condition is not the source of the flow.
				if (token instanceof TokenComparationValue) {
					if (!((TokenComparationValue) token).getQuestion().equals(flow.getOrigin())) {
						question = ((TokenComparationValue) token).getQuestion();
					}
				} else if (token instanceof TokenComparationAnswer) {
					if (!((TokenComparationAnswer) token).getQuestion().equals(flow.getOrigin())) {
						question = ((TokenComparationAnswer) token).getQuestion();
					}
				}
				if (question != null) {
					Set<List<Flow>> paths = flow.getForm().getAllFlowsFromOriginToDestiny(question, flow.getOrigin(),
							getQuestionIndex(flow), getComputedFlowView(flow));
					for (List<Flow> path : paths) {
						for (Flow pathFlow : path) {
							for (Token tokenPath : pathFlow.getCondition()) {
								if (isEquals(tokenPath, token)) {
									assertTrue(false, new FlowConditionIsUseless(flow, token));
								}
							}
						}
					}
					// Only check one time for all tokens.
					break;
				}
			}
		}
	}

	/**
	 * Ensures that the form's question order is only calculated once.
	 * 
	 * @param flow
	 * @return
	 */
	private HashMap<TreeObject, Integer> getQuestionIndex(Flow flow) {
		if (questionIndex == null && flow != null) {
			questionIndex = flow.getForm().getQuestionsInOrder();
		}
		return questionIndex;
	}

	private ComputedFlowView getComputedFlowView(Flow flow) {
		if (computedFlowView == null) {
			computedFlowView = flow.getForm().getComputedFlowsView();
		}
		return computedFlowView;
	}

	private boolean isEquals(Token token1, Token token2) {
		return token1.isContentEqual(token2);
	}
}
