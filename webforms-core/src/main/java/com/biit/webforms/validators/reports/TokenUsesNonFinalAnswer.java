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
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;

public class TokenUsesNonFinalAnswer extends Report {

	private final Flow flow;
	private final TokenComparationAnswer token;

	public TokenUsesNonFinalAnswer(Flow flow, TokenComparationAnswer token) {
		super(ReportLevel.ERROR, generateError(flow, token));
		this.flow = flow;
		this.token = token;
	}

	private static String generateError(Flow flow, TokenComparationAnswer token) {
		StringBuilder sb = new StringBuilder();
		sb.append("Flow: ");
		sb.append(flow);
		sb.append(" has a reference to '");
		sb.append(token.getAnswer() != null ? token.getAnswer().getPathAnswerValue() : "null");
		sb.append("' which is not a final answer.");
		return sb.toString();
	}

	public Flow getFlow() {
		return flow;
	}

	public TokenComparationAnswer getToken() {
		return token;
	}

}
