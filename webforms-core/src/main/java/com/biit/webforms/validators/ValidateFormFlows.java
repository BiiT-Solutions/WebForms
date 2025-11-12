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

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Form;

/**
 * Validates all the conditions for the form flow.
 *
 */
public class ValidateFormFlows extends SimpleValidator<Form> {

	public ValidateFormFlows() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		AllBaseQuestionHaveFlowInValidator allBaseQuestionHaveFlowInValidator = new AllBaseQuestionHaveFlowInValidator();
		if (!allBaseQuestionHaveFlowInValidator.validate(form, getReport())) {
			return;
		}

		ValidateFlow validateFlow = new ValidateFlow();
		if (!validateFlow.validate(form.getFlows(), getReport()) && isStopOnFail()) {
			return;
		}

		OthersOrphanAndUnicityValidator othersUnicityValidator = new OthersOrphanAndUnicityValidator();
		othersUnicityValidator.validate(form, getReport());

		ValidateEmptyFlows validateEmptyFlows = new ValidateEmptyFlows();
		validateEmptyFlows.validate(form, getReport());

		// FlowOriginDestinyUnicityValidator flowOriginDestinyUnicityValidator =
		// new FlowOriginDestinyUnicityValidator();
		// flowOriginDestinyUnicityValidator.validate(form, getReport());
	}

}
