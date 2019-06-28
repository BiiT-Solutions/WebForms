package com.biit.webforms.validators;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenWithQuestion;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.validators.reports.ConditionWithNotMandatoryQuestion;
import com.biit.webforms.validators.reports.InvalidFlowCondition;

/**
 * Validate flow condition. We skip others flow to avoid marking them as
 * possible error.
 * 
 *
 */
public class ValidateFlowCondition extends SimpleValidator<Flow> {

	public ValidateFlowCondition() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (!flow.isOthers()) {
			assertTrue(isConditionValid(flow), new InvalidFlowCondition(flow));
			assertFalse(hasNullValues(flow), new InvalidFlowCondition(flow));
			validateFlowConditionQuestionAreAllMandatory(flow);
			validateAnswersOfSelect(flow);
		}
	}

	private void validateAnswersOfSelect(Flow flow) {
		for (Token token : flow.getComputedCondition()) {
			if (token instanceof TokenWithQuestion && ((TokenWithQuestion) token).getQuestion() != null) {
				if (((TokenWithQuestion) token).getQuestion().getAnswerType().isChildrenAllowed()) {
					assertTrue(token instanceof TokenComparationAnswer || token instanceof TokenIn,
							new InvalidFlowCondition(flow));
				}
			}
		}
	}

	private void validateFlowConditionQuestionAreAllMandatory(Flow flow) {
		for (Token token : flow.getComputedCondition()) {
			if (token instanceof TokenWithQuestion && ((TokenWithQuestion) token).getQuestion() != null) {
				if (token instanceof TokenComparationAnswer) {
					assertTrue(((TokenComparationAnswer) token).getQuestion() instanceof SystemField
							|| ((TokenComparationAnswer) token).getQuestion().isMandatory(),
							new ConditionWithNotMandatoryQuestion(((TokenComparationAnswer) token).getQuestion(), flow));
				} else if (token instanceof TokenComparationValue) {
					assertTrue(((TokenComparationValue) token).getQuestion() instanceof SystemField
							|| ((TokenComparationValue) token).getQuestion().isMandatory(),
							new ConditionWithNotMandatoryQuestion(((TokenComparationValue) token).getQuestion(), flow));
				} else if (token instanceof TokenIn) {
					assertTrue(((TokenIn) token).getQuestion() instanceof SystemField
							|| ((TokenIn) token).getQuestion().isMandatory(), new ConditionWithNotMandatoryQuestion(
							((TokenIn) token).getQuestion(), flow));
				} else if (token instanceof TokenBetween) {
					assertTrue(((TokenBetween) token).getQuestion() instanceof SystemField
							|| ((TokenBetween) token).getQuestion().isMandatory(),
							new ConditionWithNotMandatoryQuestion(((TokenBetween) token).getQuestion(), flow));
				}
			}
		}
	}

	private boolean hasNullValues(Flow flow) {
		for (Token token : flow.getComputedCondition()) {
			if (token instanceof TokenWithQuestion) {
				if (((TokenWithQuestion) token).getQuestion() == null) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isConditionValid(Flow flow) {
		try {
			WebformsParser parser = new WebformsParser(flow.getComputedCondition().iterator());
			parser.parseCompleteExpression();
			return true;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | ExpressionNotWellFormedException | EmptyParenthesisException e) {
			return false;
		}
	}

}
