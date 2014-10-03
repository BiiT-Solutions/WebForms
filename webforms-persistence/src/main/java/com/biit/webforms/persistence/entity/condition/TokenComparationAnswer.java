package com.biit.webforms.persistence.entity.condition;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_comparation_answer")
public class TokenComparationAnswer extends Token {

	private static TokenTypes tokenTypes[] = new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE };

	@ManyToOne(fetch = FetchType.EAGER)
	private Question question;

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

	public void setContent(Question reference, TokenTypes tokenType, Answer answer) throws NotValidTokenType {
		this.question = reference;
		setType(tokenType);
		this.answer = answer;
	}

	public void setContent(TokenTypes tokenType, Answer answer) throws NotValidTokenType {
		setContent(this.question, tokenType, answer);
	}

	public static TokenComparationAnswer getToken(Question question, TokenTypes tokenType, Answer answer) {
		try {
			TokenComparationAnswer token = new TokenComparationAnswer();
			token.setContent(question, tokenType, answer);
			return token;
		} catch (NotValidTokenType e) {
			WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
			return null;
		}
	}

	public static TokenComparationAnswer getTokenEqual(Question question, Answer answer) {
		return getToken(question, TokenTypes.EQ, answer);
	}

	public static TokenComparationAnswer getTokenNotEqual(Question question, Answer answer) {
		return getToken(question, TokenTypes.NE, answer);
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName();
		}
		String answerString = null;
		if (answer != null) {
			answerString = answer.getName();
		}
		return referenceString + getType() + answerString;
	}

	public Question getQuestion() {
		return question;
	}

	public Answer getAnswer() {
		return answer;
	}
}
