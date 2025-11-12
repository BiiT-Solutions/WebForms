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

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormAnswerNotFound extends Report {

	private BaseForm formWithElement;
	private BaseForm formWithoutElement;
	private TreeObject elementMissed;

	public FormAnswerNotFound(BaseForm formWithElement, BaseForm formWithoutElement, TreeObject elementMissed) {
		super(ReportLevel.ERROR, generateReport(formWithElement, formWithoutElement, elementMissed));
		this.formWithElement = formWithElement;
		this.formWithoutElement = formWithoutElement;
		this.elementMissed = elementMissed;
	}

	private static String generateReport(BaseForm formWithElement, BaseForm formWithoutElement, TreeObject elementMissed) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithoutElement.getLabel());
		sb.append("' Version '");
		sb.append(formWithoutElement.getVersion());
		sb.append("' has answer '");
		sb.append(elementMissed.getPathName());
		sb.append("' and it's not found in ");
		sb.append("Form '");
		sb.append(formWithoutElement.getLabel());
		sb.append("' Version '");
		sb.append(formWithoutElement.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getFormWithElement() {
		return formWithElement;
	}

	public BaseForm getFormWithoutElement() {
		return formWithoutElement;
	}

	public TreeObject getElementMissed() {
		return elementMissed;
	}
}
