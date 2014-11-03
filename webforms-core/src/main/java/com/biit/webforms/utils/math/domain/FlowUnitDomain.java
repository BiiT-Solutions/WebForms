package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;

/**
 * Flow unit domain Is the domain that exists at every question It can be
 * created only if the union of flow domain restrictions is complete.
 * 
 */
public class FlowUnitDomain {

	public FlowUnitDomain(Form form, BaseQuestion from) throws BadFormedExpressions, IncompleteLogic, RedundantLogic {
		Set<Flow> flowQ1 = form.getFlowsFrom(from);
		System.out.println(flowQ1);
		List<FlowDomain> domains = getFlowDomains(flowQ1);
		checkUnicity(domains);
		checkCompleteness(domains);

	}

	private void checkUnicity(List<FlowDomain> domains) throws RedundantLogic {
		for(int i=0;i<domains.size();i++){
			for(int j=i+1;j<domains.size();j++){
				System.out.println(i+" "+domains.get(i));
				System.out.println(j+" "+domains.get(j));
				FlowDomain intersection = domains.get(i).intersection(domains.get(j));
				System.out.println("Interseccion "+intersection);
				if(!intersection.isEmpty()){
					throw new RedundantLogic();
				}
			}
		}
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
			FlowDomain flowDomain = FlowDomain.flowDomain(flow);
			domains.add(flowDomain);
			System.out.println(flowDomain);
		}		

		return domains;
	}

}
