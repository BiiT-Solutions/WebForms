package com.biit.webforms.validators;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.validators.reports.InvalidFlowSubformat;

/**
 * Validate the user of subformats in the condition of a flow.
 * 
 * Avoid others rules to do not mark them as the source of problem. 
 *
 */
public class ValidateFlowSubformat extends SimpleValidator<Flow> {

	public ValidateFlowSubformat() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (!flow.isOthers()) {
			TokenComparationValue invalidToken = getInvalidToken(flow);
			assertTrue(invalidToken == null, new InvalidFlowSubformat(flow,
					invalidToken));
		}
	}

	private TokenComparationValue getInvalidToken(Flow flow) {
		for (Token token : flow.getComputedCondition()) {
			if (token instanceof TokenComparationValue) {
				if (((TokenComparationValue) token).getQuestion()
						.getAnswerType().equals(AnswerType.INPUT)) {
					if (!(((TokenComparationValue) token).getQuestion()
							.getAnswerFormat()
							.isSubformat(((TokenComparationValue) token)
									.getSubformat()))) {
						return (TokenComparationValue) token;
					}
				}
			}
		}
		return null;
	}
}
