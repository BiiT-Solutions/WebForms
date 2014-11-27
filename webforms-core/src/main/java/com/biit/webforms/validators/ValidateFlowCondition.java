package com.biit.webforms.validators;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.validators.reports.InvalidFlowCondition;

public class ValidateFlowCondition extends SimpleValidator<Flow> {

	public ValidateFlowCondition() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		assertTrue(isConditionValid(flow), new InvalidFlowCondition(flow));
	}

	private boolean isConditionValid(Flow flow) {
		// Translation
		try {
			WebformsParser parser = new WebformsParser(flow.getCondition().iterator());
			parser.parseCompleteExpression();
			return true;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | ExpressionNotWellFormedException | EmptyParenthesisException e) {
			return false;
		}
	}

}
