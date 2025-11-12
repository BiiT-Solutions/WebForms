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

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;

/**
 * Empty flows can only be defined if no other flows are defined.
 */
public class ValidateEmptyFlows extends SimpleValidator<Form> {

	public ValidateEmptyFlows() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		ComputedFlowView computedFlowView = form.getComputedFlowsView();

		LinkedHashSet<BaseQuestion> baseQuestions = form.getAllChildrenInHierarchy(BaseQuestion.class);

		for (BaseQuestion baseQuestion : baseQuestions) {
			Set<Flow> flows = computedFlowView.getFlowsByOrigin(baseQuestion);
			if (flows != null) {
				for (Flow flow : flows) {
					assertTrue(!(isEmptyFlow(flow) && flows.size() > 1), new EmptyFlowIsNotAlone(flow));
				}
			}
		}
	}

	private boolean isEmptyFlow(Flow flow) {
		// Token empty can be used with others flows. Is an exception.
		for (Token token : flow.getComputedCondition()) {
			if (Objects.deepEquals(token.getType(), TokenTypes.EMPTY)) {
				return false;
			}
		}
		return flow.getComputedCondition().isEmpty();
	}

}
