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
