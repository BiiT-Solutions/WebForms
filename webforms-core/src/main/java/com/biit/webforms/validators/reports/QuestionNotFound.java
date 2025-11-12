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

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.BaseForm;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class QuestionNotFound extends Report {

	private BaseForm webform;
	private Question question;

	public QuestionNotFound(BaseForm form, Question abcdQuestion) {
		super(ReportLevel.ERROR, generateReport(form, abcdQuestion));
		this.webform = form;
		this.question = abcdQuestion;
	}

	private static String generateReport(BaseForm form, Question abcdQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form);
		sb.append("' doesn't contain question '");
		sb.append(abcdQuestion.getPathName());
		sb.append("'.");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

	public BaseForm getWebform() {
		return webform;
	}

}
