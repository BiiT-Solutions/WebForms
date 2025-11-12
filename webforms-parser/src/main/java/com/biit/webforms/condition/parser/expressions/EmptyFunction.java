package com.biit.webforms.condition.parser.expressions;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
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

import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.flow.FormWalker;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.ITokenQuestion;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.math.domain.range.QuestionValueDomain;
import com.biit.webforms.utils.parser.Expression;

/**
 * Expression that encapsulates a token of type empty
 *
 */
public class EmptyFunction extends Expression implements WebformsExpression {

	private TokenEmpty token;

	public EmptyFunction(TokenEmpty token) {
		this.token = token;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.toString());
	}

	@Override
	public Expression flatten() {
		return null;
	}

	@Override
	public Expression negate() {
		return null;
	}

	@Override
	public List<Expression> getAll(Class<?> clazz) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if (clazz.isInstance(this)) {
			retrieved.add(this);
		}
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> tokenClazz) {
		List<Token> retrieved = new ArrayList<Token>();
		if (tokenClazz.isInstance(token)) {
			retrieved.add(token);
		}
		return retrieved;
	}

	@Override
	public IDomain getDomain() {
		return QuestionValueDomain.generateQuestionValueDomain(token);
	}

	@Override
	public boolean checkBlockByMinTerms(Form form, BaseQuestion element) {
		if (token instanceof ITokenQuestion) {
			WebformsBaseQuestion question = token.getQuestion();
			if (FormWalker.anyPathFromOriginDoesntPassThrough(form, null, element, question)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean evaluate() {
		if (token instanceof ITokenQuestion) {
			return ((ITokenQuestion) token).evaluate();
		}
		return null;
	}

}
