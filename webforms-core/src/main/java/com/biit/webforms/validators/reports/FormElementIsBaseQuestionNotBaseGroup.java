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

public class FormElementIsBaseQuestionNotBaseGroup extends Report {

	private BaseForm formWithQuestion;
	private BaseForm formWithGroup;
	private TreeObject elementAsQuestion;

	public FormElementIsBaseQuestionNotBaseGroup(BaseForm formWithQuestion, BaseForm formWithGroup,
			TreeObject elementAsQuestion) {
		super(ReportLevel.ERROR, generateReport(formWithQuestion, formWithGroup, elementAsQuestion));
		this.formWithQuestion = formWithQuestion;
		this.formWithGroup = formWithGroup;
		this.elementAsQuestion = elementAsQuestion;
	}

	private static String generateReport(BaseForm formWithQuestion, BaseForm formWithGroup, TreeObject elementAsQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(formWithQuestion.getLabel());
		sb.append("' Version '");
		sb.append(formWithQuestion.getVersion());
		sb.append("' element '");
		sb.append(elementAsQuestion.getPathName());
		sb.append("' is a question and is found as a group in ");
		sb.append("Form '");
		sb.append(formWithGroup.getLabel());
		sb.append("' Version '");
		sb.append(formWithGroup.getVersion());
		sb.append("'.");
		return sb.toString();
	}

	public BaseForm getFormWithQuestion() {
		return formWithQuestion;
	}

	public BaseForm getFormWithGroup() {
		return formWithGroup;
	}

	public TreeObject getElementAsQuestion() {
		return elementAsQuestion;
	}
}
