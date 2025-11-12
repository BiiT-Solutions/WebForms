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
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Question;

/**
 * Report when one or more dynamic answers reference the same element as a
 * previous one.
 *
 */
public class MultipleDynamicAnswersReferenceTheSameQuestion extends Report {

	private Question question;
	private DynamicAnswer answer;

	public MultipleDynamicAnswersReferenceTheSameQuestion(Question question, DynamicAnswer answer) {
		super(ReportLevel.ERROR, generateReport(question, answer));
		this.question = question;
		this.answer = answer;
	}

	private static String generateReport(Question question, DynamicAnswer answer) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.getPathName());

		sb.append("' has one or more dynamic answers that refere '" + answer.getReference() + "'");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

	public DynamicAnswer getAnswer() {
		return answer;
	}
}
