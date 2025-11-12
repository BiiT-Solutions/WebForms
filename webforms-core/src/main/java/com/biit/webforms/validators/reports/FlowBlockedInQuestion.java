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

import com.biit.form.entity.BaseQuestion;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FlowBlockedInQuestion extends Report {
	
	private final BaseQuestion element;

	public FlowBlockedInQuestion(BaseQuestion element) {
		super(ReportLevel.ERROR, generateMessage(element));
		this.element = element;
	}

	private static String generateMessage(BaseQuestion element) {
		return "The flow in question '"+element.getPathName()+"' will be blocked as one or more exit flow conditions use questions that can be bypassed.";
	}

	public BaseQuestion getElement() {
		return element;
	}

}
