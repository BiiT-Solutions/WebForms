package com.biit.webforms.persistence.entity.condition;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_comparation_answer")
public class TokenComparationAnswer extends TokenWithQuestion implements ITokenQuestion {
	private static final long serialVersionUID = 2099093205161281219L;

	private static TokenTypes tokenTypes[] = new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE };

	// Evaluation value is false by default
	private transient Boolean evaluationValue = null;

	@ManyToOne(fetch = FetchType.EAGER)
	private Answer answer;

	public TokenComparationAnswer() {
		super();
	}

	public TokenComparationAnswer(TokenTypes tokenType) throws NotValidTokenType {
		super(tokenType);
	}

	@Override
	public TokenTypes[] getValidTokenTypes() {
		return tokenTypes;
	}

	public void setContent(WebformsBaseQuestion reference, TokenTypes tokenType, Answer answer)
			throws NotValidTokenType {
		setQuestion(reference);
		setType(tokenType);
		this.answer = answer;
	}

	public void setContent(TokenTypes tokenType, Answer answer) throws NotValidTokenType {
		setContent(getQuestion(), tokenType, answer);
	}

	public static TokenComparationAnswer getToken(WebformsBaseQuestion question, TokenTypes tokenType, Answer answer) {
		try {
			TokenComparationAnswer token = new TokenComparationAnswer();
			token.setContent(question, tokenType, answer);
			return token;
		} catch (NotValidTokenType e) {
			WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
			return null;
		}
	}

	public static TokenComparationAnswer getTokenEqual(WebformsBaseQuestion question, Answer answer) {
		return getToken(question, TokenTypes.EQ, answer);
	}

	public static TokenComparationAnswer getTokenNotEqual(WebformsBaseQuestion question, Answer answer) {
		return getToken(question, TokenTypes.NE, answer);
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getName();
		}
		String answerString = null;
		if (answer != null) {
			answerString = answer.getName();
		}
		return referenceString + getType() + answerString;
	}

	public Answer getAnswer() {
		return answer;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenComparationAnswer) {
			super.copyData(object);
			TokenComparationAnswer token = (TokenComparationAnswer) object;
			answer = token.getAnswer();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenComparationAnswer.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		if (getQuestion() != null) {
			setQuestion((WebformsBaseQuestion) mappedElements.get(getQuestion().getOriginalReference()));
			if (answer != null) {
				answer = (Answer) mappedElements.get(answer.getOriginalReference());
			}
		}
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getPathName().replaceAll("[^A-Za-z0-9_./]", "_");
		}
		String answerString = null;
		if (answer != null) {
			answerString = answer.getName();
		}
		return referenceString + getType().getExpressionSimplifierRepresentation() + answerString;
	}

	@Override
	public String getExpressionEditorRepresentation() {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getPathName();
		}
		String answerString = null;
		if (answer != null) {
			answerString = answer.getName();
		}
		return referenceString + getType().getExpressionEditorRepresentation() + answerString;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	/**
	 * Compares two token ComparationAnswer. it must be of token comparation type.
	 */
	@Override
	public boolean isContentEqual(Token token) {
		if (token instanceof TokenComparationAnswer) {
			TokenComparationAnswer comparationAnswer = (TokenComparationAnswer) token;
			if (super.isContentEqual(token)) {
				if (!answer.getPathName().equals(comparationAnswer.answer.getPathName())) {
					return false;
				}

				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean evaluate() {
		return evaluationValue;
	}

	public void evaluate(List<String> answers) {
		if (answers == null) {
			evaluationValue = null;
			return;
		}

		evaluationValue = false;
		for (String answer : answers) {
			if (this.answer.getName().equals(answer)) {
				evaluationValue = true;
				return;
			}
		}
	}
}
