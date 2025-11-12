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
import com.biit.webforms.persistence.entity.Question;

public class DynamicAnswerReferenceInvalid extends Report {

	private Question question;
	private Question reference;

	public DynamicAnswerReferenceInvalid(Question question,Question reference) {
		super(ReportLevel.ERROR, generateReport(question,reference));
		this.question = question;
		this.reference = reference;
	}

	private static String generateReport(Question question,Question reference) {
		StringBuilder sb = new StringBuilder();
		sb.append("Question '");
		sb.append(question.getPathName());
		sb.append("' references future question '");
		sb.append(reference.getPathName());
		sb.append(".");
		return sb.toString();
	}

	public Question getQuestion() {
		return question;
	}

	public Question getReference() {
		return reference;
	}

}
