package com.biit.webforms.exporters.xforms;

import java.util.HashMap;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;

/**
 * This dummy token indicates that the "OTHERS" rule must be answered. This token is translated to Orbeon as
 * string-lenght()>0.
 */
public class TokenOthersMustBeAnswered extends Token {
	private static final long serialVersionUID = -5314559285041409174L;
	private BaseQuestion question;

	public TokenOthersMustBeAnswered() {
		super();
	}

	public TokenOthersMustBeAnswered(BaseQuestion question) {
		super();
		this.question = question;
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

		return "others(" + referenceString + ")";
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName().replaceAll("[^A-Za-z0-9_.]", "_");
		}

		return "others[" + referenceString + "]";
	}

	public BaseQuestion getQuestion() {
		return question;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenOthersMustBeAnswered) {
			super.copyData(object);
			TokenOthersMustBeAnswered token = (TokenOthersMustBeAnswered) object;
			question = token.getQuestion();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenOthersMustBeAnswered.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		question = (Question) mappedElements.get(question.getComparationId());
	}

}
