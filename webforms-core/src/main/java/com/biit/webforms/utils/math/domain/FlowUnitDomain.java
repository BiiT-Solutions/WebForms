package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

/**
 * Flow unit domain Is the domain that exists at every question It can be
 * created only if the union of flow domain restrictions is complete.
 * 
 */
public class FlowUnitDomain {

	public FlowUnitDomain(Form form, BaseQuestion from) throws BadFormedExpressions {

		Set<Flow> flowQ1 = form.getFlowsFrom(from);
		System.out.println(flowQ1);
		getExpressions(flowQ1);

	}

	private List<Expression> getExpressions(Set<Flow> flows) throws BadFormedExpressions {
		List<Expression> expressions = new ArrayList<>();

		for (Flow flow : flows) {
			FlowDomain flowDomain = new FlowDomain(flow);
		}

		return null;

	}

}
