package com.biit.webforms.persistence.entity.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_between")
public class TokenBetween extends TokenComplex {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Question question;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AnswerSubformat subformat;

	@Enumerated(EnumType.STRING)
	private DatePeriodUnit datePeriodUnit;

	private String valueStart;

	private String valueEnd;

	@Override
	public List<Token> getSimpleTokens() {
		List<Token> simpleTokens = new ArrayList<Token>();
		simpleTokens.add(Token.leftPar());
		simpleTokens.add(TokenComparationValue.getTokenGreaterEqual(question, subformat, datePeriodUnit, valueStart));
		simpleTokens.add(Token.and());
		simpleTokens.add(TokenComparationValue.getTokenLessEqual(question, subformat, datePeriodUnit, valueEnd));
		simpleTokens.add(Token.rigthPar());
		return simpleTokens;
	}

	public void setContent(Question reference, AnswerSubformat subformat, DatePeriodUnit datePeriodUnit,
			String valueStart, String valueEnd) throws NotValidTokenType {
		this.setType(TokenTypes.BETWEEN);
		this.question = reference;
		this.subformat = subformat;
		this.datePeriodUnit = datePeriodUnit;
		this.valueStart = valueStart;
		this.valueEnd = valueEnd;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public AnswerSubformat getSubformat() {
		return subformat;
	}

	public void setSubformat(AnswerSubformat subformat) {
		this.subformat = subformat;
	}

	public DatePeriodUnit getDatePeriodUnit() {
		return datePeriodUnit;
	}

	public void setDatePeriodUnit(DatePeriodUnit datePeriodUnit) {
		this.datePeriodUnit = datePeriodUnit;
	}

	public String getValueStart() {
		return valueStart;
	}

	public void setValueStart(String valueStart) {
		this.valueStart = valueStart;
	}

	public String getValueEnd() {
		return valueEnd;
	}

	public void setValueEnd(String valueEnd) {
		this.valueEnd = valueEnd;
	}
	
	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenBetween) {
			super.copyData(object);
			TokenBetween token = (TokenBetween) object;
			this.question = token.question;
			this.subformat = token.subformat;
			this.datePeriodUnit = token.datePeriodUnit;
			this.valueStart = token.valueStart;
			this.valueEnd = token.valueEnd;
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenComparationAnswer.class.getName());
		}
	}
	
	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		question = (Question) mappedElements.get(question.getComparationId());
	}
}