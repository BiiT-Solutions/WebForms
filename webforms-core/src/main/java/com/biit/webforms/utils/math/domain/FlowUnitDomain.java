package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;

/**
 * Flow unit domain Is the domain that exists at every question It can be
 * created only if the union of flow domain restrictions is complete.
 * 
 */
public class FlowUnitDomain {

	public FlowUnitDomain(Form form, BaseQuestion from) throws BadFormedExpressions, IncompleteLogic {
		Set<Flow> flowQ1 = form.getFlowsFrom(from);
		System.out.println(flowQ1);
		List<FlowDomain> domains = getFlowDomains(flowQ1);
		checkCompleteness(domains);

	}

	private void checkCompleteness(List<FlowDomain> flowDomains) throws IncompleteLogic {
		FlowDomain unionflowDomain = new FlowDomain();

		for (FlowDomain flowDomain : flowDomains) {
			unionflowDomain = unionflowDomain.union(flowDomain);
		}
		if (!unionflowDomain.isComplete()) {
			throw new IncompleteLogic();
		}
	}

	private List<FlowDomain> getFlowDomains(Set<Flow> flows) throws BadFormedExpressions {

		List<FlowDomain> domains = new ArrayList<FlowDomain>();

		for (Flow flow : flows) {
			FlowDomain flowDomain = new FlowDomain(flow);
			domains.add(flowDomain);
		}

		return domains;
	}

}
