package com.biit.webforms.persistence.entity.condition;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
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

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.serialization.TokenComparationAnswerDeserializer;
import com.biit.webforms.serialization.TokenComparationAnswerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;

@Entity
@JsonDeserialize(using = TokenComparationAnswerDeserializer.class)
@JsonSerialize(using = TokenComparationAnswerSerializer.class)
@Table(name = "token_comparation_answer")
public class TokenComparationAnswer extends TokenWithQuestion implements ITokenQuestion {
	private static final long serialVersionUID = 2099093205161281219L;

	private static final TokenTypes[] TOKEN_TYPES = new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE };

	// Evaluation value is false by default
	private transient Boolean evaluationValue = null;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "answer")
	private Answer answer;

	//Only for json serialization.
	@Transient
	private transient List<String> answerReferencePath;

	public TokenComparationAnswer() {
		super();
	}

	public TokenComparationAnswer(TokenTypes tokenType) throws NotValidTokenType {
		super(tokenType);
	}

	@Override
	public TokenTypes[] getValidTokenTypes() {
		return TOKEN_TYPES;
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
			throw new NotValidStorableObjectException(
					object.getClass().getName() + " is not compatible with " + TokenComparationAnswer.class.getName());
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
			// Sliders store value as double but names are integer.
			if (getQuestion().getAnswerType().equals(AnswerType.SINGLE_SELECTION_SLIDER)) {
				if (Integer.parseInt(this.answer.getName()) == (int) (Double.parseDouble(answer))) {
					evaluationValue = true;
					return;
				}
			}
			if (this.answer.getName().equals(answer)) {
				evaluationValue = true;
				return;
			}
		}
	}

	public List<String> getAnswerReferencePath() {
		return answerReferencePath;
	}

	public void setAnswerReferencePath(List<String> answerReferencePath) {
		this.answerReferencePath = answerReferencePath;
	}
}
