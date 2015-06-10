package com.biit.webforms.xforms;

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
