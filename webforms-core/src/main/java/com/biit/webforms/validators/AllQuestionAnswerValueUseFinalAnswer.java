package com.biit.webforms.validators;

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

import java.util.Iterator;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.validators.reports.TokenUsesNonFinalAnswer;

/**
 * This validator checks that all the flows that use Q=A tokens do not use answer that are not final. A final answer is
 * one that doesn't have sub answers or any kind of child.
 *
 * We skip other rules to avoid marking them as the cause of error.
 *
 */
public class AllQuestionAnswerValueUseFinalAnswer extends SimpleValidator<Flow> {

	public AllQuestionAnswerValueUseFinalAnswer() {
		super(Flow.class);
	}

	@Override
	protected void validateImplementation(Flow flow) {
		if (!flow.isOthers()) {
			Iterator<Token> itr = flow.getComputedCondition().iterator();

			while (itr.hasNext()) {
				Token token = itr.next();
				if (token instanceof TokenComparationAnswer) {
					if (((TokenComparationAnswer) token).getAnswer() != null) {
						assertTrue(((TokenComparationAnswer) token).getAnswer().isFinalAnswer(),
								new TokenUsesNonFinalAnswer(flow, (TokenComparationAnswer) token));
					} else {
						assertTrue(false, new TokenUsesNonFinalAnswer(flow, (TokenComparationAnswer) token));
					}
				}
			}
		}
	}
}
