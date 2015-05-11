package com.biit.webforms.persistence.entity.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_between")
public class TokenBetween extends TokenComplex implements ITokenQuestion {
	private static final long serialVersionUID = -8760649306071342145L;

	@ManyToOne(fetch = FetchType.EAGER, optional=false)
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

	@Override
	public String toString() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName();
			if (subformat == AnswerSubformat.DATE_PERIOD) {
				referenceString += " (" + datePeriodUnit + ")";
			}
		}
		String answerStart = null;
		if (valueStart != null) {
			answerStart = valueStart;
		}
		String answerEnd = null;
		if (valueEnd != null) {
			answerEnd = valueEnd;
		}

		return referenceString + " " + getType() + " (" + answerStart + ", " + answerEnd + ")";
	}

	public static Token getBetween(Question question, DatePeriodUnit datePeriodUnit, String valueStart, String valueEnd) {
		try {
			TokenBetween token = new TokenBetween();
			token.setType(TokenTypes.BETWEEN);
			token.question = question;
			token.setContent(datePeriodUnit, valueStart, valueEnd);
			return token;
		} catch (NotValidTokenType e) {
			WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
			return null;
		}
	}

	public void setContent(DatePeriodUnit datePeriodUnit, String valueStart, String valueEnd) {
		if (datePeriodUnit == null) {
			subformat = question.getAnswerSubformat();
		} else {
			subformat = AnswerSubformat.DATE_PERIOD;
		}
		this.datePeriodUnit = datePeriodUnit;
		this.valueStart = valueStart;
		this.valueEnd = valueEnd;
	}

	@Override
	public String getExpressionEditorRepresentation() {
		return toString();
	}

	/**
	 * Compares two token between. it must be of token between type.
	 */
	@Override
	public boolean isContentEqual(Token token) {
		if (token instanceof TokenBetween) {
			TokenBetween between = (TokenBetween) token;
			if (super.isContentEqual(token)) {
				if (!question.getPathName().equals(between.question.getPathName())) {
					return false;
				}

				if (subformat != between.subformat) {
					return false;
				}

				if (datePeriodUnit != null && datePeriodUnit != between.datePeriodUnit) {
					return false;
				}

				if (valueStart != null && !valueStart.equals(between.valueStart)) {
					return false;
				}

				if (valueEnd != null && !valueEnd.equals(between.valueEnd)) {
					return false;
				}

				return true;
			}
		}
		return false;
	}
}