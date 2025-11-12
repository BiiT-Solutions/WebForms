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

import java.util.HashMap;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;

public class TokenInheritRelevant extends Token {
	private static final long serialVersionUID = 5096519176634657976L;

	private BaseQuestion inheritedQuestion;

	public TokenInheritRelevant() {
		super();
	}

	public TokenInheritRelevant(BaseQuestion inheritedQuestion) {
		super();
		this.inheritedQuestion = inheritedQuestion;
	}

	@Override
	public TokenTypes getType() {
		return TokenTypes.NE;
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (inheritedQuestion != null) {
			referenceString = inheritedQuestion.getPathName();
		}

		return "instance('visible')/" + referenceString + getType() + "false";
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (inheritedQuestion != null) {
			referenceString = inheritedQuestion.getPathName().replaceAll("[^A-Za-z0-9_.]", "_");
		}

		return "sameas[" + referenceString + "]";
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenInheritRelevant) {
			super.copyData(object);
			TokenInheritRelevant token = (TokenInheritRelevant) object;
			inheritedQuestion = token.getInheritedQuestion();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenInheritRelevant.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		inheritedQuestion = (Question) mappedElements.get(inheritedQuestion.getComparationId());
	}

	public BaseQuestion getInheritedQuestion() {
		return inheritedQuestion;
	}

}
