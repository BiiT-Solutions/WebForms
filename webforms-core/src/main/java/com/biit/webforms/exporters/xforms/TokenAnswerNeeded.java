package com.biit.webforms.exporters.xforms;

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

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;

import java.util.HashMap;

/**
 * Used for representing that a previous question must be answered. As string-length('previous-question') &gt; 0 This token
 * must not been stored in database.
 */
public class TokenAnswerNeeded extends Token {
	private static final long serialVersionUID = 1610423674018638342L;
	private BaseQuestion question;
	private boolean dateField;

	public TokenAnswerNeeded() {
		super();
	}

	public TokenAnswerNeeded(BaseQuestion question, boolean dateField) {
		super();
		this.question = question;
		this.dateField = dateField;
	}

	@Override
	public TokenTypes getType() {
		return TokenTypes.GT;
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName();
		}

		return "length(" + referenceString + ")" + getType() + "0";
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName().replaceAll("[^A-Za-z0-9_.]", "_");
		}

		return "answer[" + referenceString + "]";
	}

	public BaseQuestion getQuestion() {
		return question;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenAnswerNeeded) {
			super.copyData(object);
			TokenAnswerNeeded token = (TokenAnswerNeeded) object;
			question = token.getQuestion();
			dateField = token.isDateField();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenAnswerNeeded.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		question = (Question) mappedElements.get(question.getComparationId());
	}

	public boolean isDateField() {
		return dateField;
	}

}
