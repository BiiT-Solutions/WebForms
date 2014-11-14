package com.biit.webforms.xforms;

import java.util.HashMap;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;

/**
 * Used for representing that a previous answer must be answered. As string-length('previous-question')>0 This token
 * must not been stored in database.
 */
public class TokenAnswerNeeded extends Token {

	private Question question;

	public TokenAnswerNeeded() {
		super();
	}

	public TokenAnswerNeeded(Question question) {
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

		return "length(" + referenceString + ")" + getType() + "0";
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName();
		}

		return "answer[" + referenceString + "]";
	}

	public Question getQuestion() {
		return question;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenAnswerNeeded) {
			super.copyData(object);
			TokenAnswerNeeded token = (TokenAnswerNeeded) object;
			question = token.getQuestion();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenAnswerNeeded.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		question = (Question) mappedElements.get(question.getComparationId());
	}

}
