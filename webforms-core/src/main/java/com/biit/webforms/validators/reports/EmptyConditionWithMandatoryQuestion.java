package com.biit.webforms.validators.reports;

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

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Flow;

public class EmptyConditionWithMandatoryQuestion extends Report {

	private Flow flow;

	public EmptyConditionWithMandatoryQuestion(Flow flow) {
		super(ReportLevel.ERROR, "Flow '" + flow + "' origin '" + flow.getOrigin().getPathName()
				+ "' is mandatory and conditions is marked as '" + TokenTypes.EMPTY + "'.");
		this.flow = flow;
	}

	public Flow getFlow() {
		return flow;
	}

}
