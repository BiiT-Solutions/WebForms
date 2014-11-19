package com.biit.webforms.persistence.entity.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_in")
public class TokenIn extends TokenComplex {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Question question;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "tokenIn")
	@BatchSize(size = 100)
	@OrderBy(value = "sortSeq ASC")
	private List<TokenInValue> values;

	public TokenIn() {
		values = new ArrayList<TokenInValue>();
	}

	@Override
	public List<Token> getSimpleTokens() {
		List<Token> simpleTokens = new ArrayList<Token>();
		simpleTokens.add(Token.leftPar());
		if (!values.isEmpty()) {
			simpleTokens.add(TokenComparationAnswer.getTokenEqual(question, values.get(0).getAnswerValue()));
			for (int i = 1; i < values.size(); i++) {
				simpleTokens.add(TokenComparationValue.or());
				simpleTokens.add(TokenComparationAnswer.getTokenEqual(question, values.get(i).getAnswerValue()));
			}

		}
		simpleTokens.add(Token.rigthPar());
		return simpleTokens;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenIn) {
			super.copyData(object);
			TokenIn token = (TokenIn) object;
			this.question = token.question;
			this.values = new ArrayList<>();
			for (TokenInValue value : token.values) {
				TokenInValue valueCopy = value.generateCopy();
				valueCopy.setTokenIn(this);
				this.values.add(valueCopy);				
			}
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenComparationAnswer.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		question = (Question) mappedElements.get(question.getComparationId());
		for (TokenInValue value : values) {
			if (value.getAnswerValue() != null) {
				value.setAnswerValue((Answer) mappedElements.get(value.getAnswerValue().getComparationId()));
			}
		}
	}

	public static Token getTokenIn(Question question, Answer... answers) {
		try {
			TokenIn token = new TokenIn();
			token.setType(TokenTypes.IN);
			token.question = question;
			for (Answer answer : answers) {
				TokenInValue value = new TokenInValue();
				value.setAnswerValue(answer);
				token.values.add(value);
			}
			return token;
		} catch (NotValidTokenType e) {
			WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
			return null;
		}
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getName();
		}
		String answerString = null;
		if (values != null) {
			answerString = values.toString();
		}
		return referenceString + " " + getType() + " " + answerString;
	}

	public Question getQuestion() {
		return question;
	}

	public List<Answer> getAnswerValues() {
		List<Answer> answers = new ArrayList<Answer>();
		for (TokenInValue value : values) {
			answers.add(value.getAnswerValue());
		}
		return answers;
	}

	public void setAnswers(Answer[] answers) {
		values.clear();
		for (Answer answer : answers) {
			TokenInValue value = new TokenInValue();
			value.setAnswerValue(answer);
			values.add(value);
			value.setTokenIn(this);
		}
	}
	
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		HashSet<StorableObject> innerStorableObjects = new HashSet<StorableObject>();
		
		for(TokenInValue value: values){
			innerStorableObjects.add(value);
		}
		
		return innerStorableObjects;
	}
	
	@Override
	public void resetIds() {
		super.resetIds();
		for (TokenInValue value : values) {
			value.resetIds();
		}
	}
}
