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
import com.biit.form.entity.BaseQuestion;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class QuestionCompatibilityError extends Report {

	public QuestionCompatibilityError(BaseQuestion question1, BaseForm form, BaseQuestion question2) {
		super(ReportLevel.ERROR, generateReport(question1, form, question2));
	}

	private static String generateReport(BaseQuestion question1, BaseForm form, BaseQuestion question2) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(form.getLabel());
		sb.append("' Version ");
		sb.append(form.getVersion());
		sb.append(" Question '");
		sb.append(question2.getPathName());
		sb.append("' is not compatible.");
		if (question2 instanceof com.biit.abcd.persistence.entity.Question) {
			sb.append(" '");
			sb.append(((com.biit.abcd.persistence.entity.Question) question2).getAnswerType());
			sb.append("' ");
			if (((com.biit.abcd.persistence.entity.Question) question2).getAnswerFormat() != null) {
				sb.append(" Format '");
				sb.append(((com.biit.abcd.persistence.entity.Question) question2).getAnswerFormat());
				sb.append("' ");
			}
		}

		return sb.toString();
	}

}
