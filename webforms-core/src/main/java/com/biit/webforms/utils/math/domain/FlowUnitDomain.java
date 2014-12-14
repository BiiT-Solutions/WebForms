package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

/**
 * Flow unit domain Is the domain that exists at every question It can be created only if the union of flow domain
 * restrictions is complete.
 * 
 */
public class FlowUnitDomain {

	public FlowUnitDomain(Form form, BaseQuestion from) throws BadFormedExpressions, IncompleteLogic, RedundantLogic {
		Set<Flow> flowQ1 = form.getFlowsFrom(from);
		List<IDomain> domains = getFlowDomains(flowQ1);
		checkUnicity(domains);
		checkCompleteness(domains);

	}

	private void checkUnicity(List<IDomain> domains) throws RedundantLogic {
		System.out.println("Unicity check, Domains:" + domains);
		for (int i = 0; i < domains.size(); i++) {
			for (int j = i + 1; j < domains.size(); j++) {
				System.out.println(domains.get(i).getClass());
				System.out.println("Intersection Bef: "+domains.get(i)+" "+(domains.get(j)));
				IDomain intersection = domains.get(i).intersect(domains.get(j));
				System.out.println("Intersection: "+intersection);
				if (!intersection.isEmpty()) {
					throw new RedundantLogic();
				}
			}
		}
	}

	private void checkCompleteness(List<IDomain> flowDomains) throws IncompleteLogic {
		IDomain unionflowDomain = null;

		for (IDomain flowDomain : flowDomains) {
			if (unionflowDomain == null) {
				unionflowDomain = flowDomain;
			} else {
				unionflowDomain = unionflowDomain.union(flowDomain);
			}
			System.out.println(unionflowDomain);
		}
		if (!unionflowDomain.isComplete()) {
			throw new IncompleteLogic();
		}
	}

	private List<IDomain> getFlowDomains(Set<Flow> flows) throws BadFormedExpressions {

		List<IDomain> domains = new ArrayList<>();

		for (Flow flow : flows) {
			IDomain flowDomain = getDomain(flow);
			domains.add(flowDomain);
		}

		return domains;
	}

	private IDomain getDomain(Flow flow) {
		WebformsParser parser = new WebformsParser(flow.getCondition().iterator());
		try {
			return ((WebformsExpression) parser.parseExpression()).getDomain();
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | EmptyParenthesisException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

}
