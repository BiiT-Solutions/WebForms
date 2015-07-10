package com.biit.webforms.validators;

import java.util.Iterator;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.validators.reports.TokenUsesNonFinalAnswer;

/**
 * This validator checks that all the flows that use Q=A tokens do not use answer that are not final. A final answer is
 * one that doesn't have sub answers or any kind of child.
 * 
 * We skip other rules to avoid marking them as the cause of error.
 * 
 */
public class AllQuestionAnswerValueUseFinalAnswer extends SimpleValidator<Flow> {

	public AllQuestionAnswerValueUseFinalAnswer() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (!flow.isOthers()) {
			Iterator<Token> itr = flow.getComputedCondition().iterator();

			while (itr.hasNext()) {
				Token token = itr.next();
				if (token instanceof TokenComparationAnswer) {
					if (((TokenComparationAnswer) token).getAnswer() != null) {
						assertTrue(((TokenComparationAnswer) token).getAnswer().isFinalAnswer(),
								new TokenUsesNonFinalAnswer(flow, (TokenComparationAnswer) token));
					} else {
						assertTrue(false, new TokenUsesNonFinalAnswer(flow, (TokenComparationAnswer) token));
					}
				}
			}
		}
	}
}
