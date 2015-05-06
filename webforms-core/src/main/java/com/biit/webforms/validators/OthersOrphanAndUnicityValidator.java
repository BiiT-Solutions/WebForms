package com.biit.webforms.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.validators.reports.OthersOrphanAt;
import com.biit.webforms.validators.reports.OthersUnicityBrokenAt;

/**
 * 
 * Checks that there is only only one other flow for each normal flow from one
 * question.
 * 
 * Checks that there are no orphan "others" flows (Single others flow without a
 * normal flow from one question.
 * 
 */
public class OthersOrphanAndUnicityValidator extends SimpleValidator<Form> {

	public OthersOrphanAndUnicityValidator() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		HashMap<TreeObject, List<Flow>> flowsByOrigin = new HashMap<>();
		Set<Flow> flows = form.getFlows();
		for (Flow flow : flows) {
			TreeObject origin = flow.getOrigin();
			if (!flowsByOrigin.containsKey(origin)) {
				flowsByOrigin.put(origin, new ArrayList<Flow>());
			}
			flowsByOrigin.get(origin).add(flow);
		}

		for (TreeObject origin : flowsByOrigin.keySet()) {
			List<Flow> flowsOfOrigin = flowsByOrigin.get(origin);
			int othersCount = 0;
			for (Flow flow : flowsOfOrigin) {
				if (flow.isOthers()) {
					othersCount++;
				}
			}
			assertTrue(othersCount <= 1, new OthersUnicityBrokenAt(origin));
			if(othersCount == 1){
				assertTrue(flowsOfOrigin.size() != 1, new OthersOrphanAt(origin));
			}
		}
	}

}
