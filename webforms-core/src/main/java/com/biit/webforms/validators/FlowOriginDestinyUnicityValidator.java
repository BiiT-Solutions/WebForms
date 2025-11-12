package com.biit.webforms.validators;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.validators.reports.MultipleEndFormsFromSameElement;
import com.biit.webforms.validators.reports.MultipleEndLoopsFromSameElement;
import com.biit.webforms.validators.reports.MultipleFlowsWithSameOriginAndDestiny;

public class FlowOriginDestinyUnicityValidator extends SimpleValidator<Form> {

	public FlowOriginDestinyUnicityValidator() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		HashMap<TreeObject, List<Flow>> flowsByOrigin = getFlowsByOrigin(form);

		for (TreeObject origin : flowsByOrigin.keySet()) {
			List<Flow> flowsOfOrigin = flowsByOrigin.get(origin);

			int endForms = 0;
			int endLoops = 0;
			Set<TreeObject> destinations = new HashSet<>();
			for (Flow flow : flowsOfOrigin) {
				if (flow.getFlowType() == FlowType.END_FORM) {
					endForms++;
					continue;
				}
				if (flow.getFlowType() == FlowType.END_LOOP) {
					endLoops++;
					continue;
				}
				if (destinations.contains(flow.getDestiny())) {
					assertTrue(false, new MultipleFlowsWithSameOriginAndDestiny(flow.getOrigin(), flow.getDestiny()));
				} else {
					destinations.add(flow.getDestiny());
				}
			}
			assertTrue(endForms <= 1, new MultipleEndFormsFromSameElement(origin));
			assertTrue(endLoops <= 1, new MultipleEndLoopsFromSameElement(origin));
		}
	}

	private HashMap<TreeObject, List<Flow>> getFlowsByOrigin(Form form) {
		HashMap<TreeObject, List<Flow>> flowsByOrigin = new HashMap<>();
		Set<Flow> flows = form.getFlows();
		for (Flow flow : flows) {
			TreeObject origin = flow.getOrigin();
			if (!flowsByOrigin.containsKey(origin)) {
				flowsByOrigin.put(origin, new ArrayList<Flow>());
			}
			flowsByOrigin.get(origin).add(flow);
		}
		return flowsByOrigin;
	}
}
