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
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.OthersConditionWithNotMandatoryQuestion;

/**
 * Only allow an 'others' based on mandatory fields
 */
public class ValidateOthersCondition extends SimpleValidator<Flow> {

	public ValidateOthersCondition() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (flow.getOrigin() instanceof Question) {
			if (!((Question) flow.getOrigin()).isMandatory()) {
				assertFalse(flow.isOthers(), new OthersConditionWithNotMandatoryQuestion((Question) flow.getOrigin()));
			}
		}
	}
}
