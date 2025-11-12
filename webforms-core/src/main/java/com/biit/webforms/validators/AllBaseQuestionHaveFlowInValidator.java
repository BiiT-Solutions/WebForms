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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.validators.reports.FormElementWithoutFlowIn;

public class AllBaseQuestionHaveFlowInValidator extends SimpleValidator<Form> {

	public AllBaseQuestionHaveFlowInValidator() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		ComputedFlowView flows = form.getComputedFlowsView();

		LinkedHashSet<TreeObject> baseQuestions = form.getAllNotHiddenChildrenInHierarchy(BaseQuestion.class);

		if (!baseQuestions.isEmpty()) {
			Iterator<TreeObject> itr = baseQuestions.iterator();
			itr.next();
			while (itr.hasNext()) {
				TreeObject element = itr.next();

				Set<Flow> flowToElement = flows.getFlowsByDestiny(element);
				assertTrue(flowToElement != null && !flowToElement.isEmpty(), new FormElementWithoutFlowIn(element));
			}
		}
	}

}
