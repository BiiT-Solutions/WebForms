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

public class FormElementIsBaseGroupNotBaseQuestion extends Report {

	private BaseForm formWithGroup;
	private BaseForm formWithQuestion;
	private TreeObject elementAsGroup;

	public FormElementIsBaseGroupNotBaseQuestion(BaseForm formWithGroup, BaseForm formWithQuestion, TreeObject elementAsGroup) {
		super(ReportLevel.ERROR, generateReport(formWithGroup, formWithQuestion, elementAsGroup));
		this.formWithGroup = formWithGroup;
		this.formWithQuestion = formWithQuestion;
		this.elementAsGroup = elementAsGroup;
	}

	private static String generateReport(BaseForm formWithGroup, BaseForm formWithQuestion, TreeObject elementAsGroup) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithGroup.getLabel());
		sb.append("' Version '");
		sb.append(formWithGroup.getVersion());
		sb.append("' element '");
		sb.append(elementAsGroup.getPathName());
		sb.append("' is a group and has been found as a question in ");
		sb.append("Form '");
		sb.append(formWithQuestion.getLabel());
		sb.append("' Version '");
		sb.append(formWithQuestion.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getFormWithGroup() {
		return formWithGroup;
	}

	public BaseForm getFormWithQuestion() {
		return formWithQuestion;
	}

	public TreeObject getElementAsGroup() {
		return elementAsGroup;
	}

}
