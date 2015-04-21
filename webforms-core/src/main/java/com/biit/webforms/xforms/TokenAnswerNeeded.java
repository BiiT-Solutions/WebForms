package com.biit.webforms.xforms;

import java.util.HashMap;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
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
