package com.biit.webforms.persistence.entity.condition;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_comparation_value")
public class TokenComparationValue extends Token {

	private static TokenTypes tokenTypes[] = new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE, TokenTypes.LT,
			TokenTypes.GT, TokenTypes.LE, TokenTypes.GE };

	@ManyToOne(fetch = FetchType.EAGER)
	private Question question;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AnswerSubformat subformat;

	@Enumerated(EnumType.STRING)
	private DatePeriodUnit datePeriodUnit;

	private String value;

	public TokenComparationValue() {
		super();
	}

	public TokenComparationValue(TokenTypes tokenType) throws NotValidTokenType {
		super(tokenType);
	}

	@Override
	public TokenTypes[] getValidTokenTypes() {
		return tokenTypes;
	}

	public void setContent(Question reference, TokenTypes tokenType, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) throws NotValidTokenType {
		this.setType(tokenType);
		this.question = reference;
		this.subformat = subformat;
		this.datePeriodUnit = datePeriodUnit;
		this.value = value;
	}

	public void setContent(TokenTypes tokenType, AnswerSubformat subformat, DatePeriodUnit datePeriodUnit, String value)
			throws NotValidTokenType {
		setContent(this.question, tokenType, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getToken(TokenTypes tokenType, Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		try {
			TokenComparationValue token = new TokenComparationValue();
			token.setContent(reference, tokenType, subformat, datePeriodUnit, value);
			return token;
		} catch (NotValidTokenType e) {
			WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
			return null;
		}
	}

	public static TokenComparationValue getTokenEqual(Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.EQ, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenNotEqual(Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.NE, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenLessThan(Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.LT, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenGreaterThan(Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.GT, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenLessEqual(Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.LE, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenGreaterEqual(Question reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.GE, reference, subformat, datePeriodUnit, value);
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName();
		}

		if (subformat == AnswerSubformat.DATE_PERIOD) {
			return referenceString + " (" + datePeriodUnit + ")" + getType() + value.substring(0, value.length());
		}

		return referenceString + getType() + value;
	}

	public String getLocalizedString(String localizedDatePeriodUnit) {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName();
		}

		if (subformat == AnswerSubformat.DATE_PERIOD) {
			return referenceString + " (" + localizedDatePeriodUnit + ")" + getType()
					+ value.substring(0, value.length());
		}

		return referenceString + getType() + value;
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName().replaceAll("[^A-Za-z0-9_./]", "_");
		}
		return referenceString + getType().getExpressionSimplifierRepresentation() + value;
	}

	@Override
	public String getExpressionEditorRepresentation() {
		String referenceString = null;
		if (question != null) {
			referenceString = question.getPathName().replaceAll("[^A-Za-z0-9_.]", "_");
		}
		return referenceString + getType().getExpressionEditorRepresentation() + value;
	}

	public Question getQuestion() {
		return question;
	}

	public String getValue() {
		return value;
	}

	public AnswerSubformat getSubformat() {
		return subformat;
	}

	public DatePeriodUnit getDatePeriodUnit() {
		return datePeriodUnit;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenComparationValue) {
			super.copyData(object);
			TokenComparationValue token = (TokenComparationValue) object;
			question = token.getQuestion();
			subformat = token.getSubformat();
			datePeriodUnit = token.getDatePeriodUnit();
			value = token.getValue();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenComparationValue.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		question = (Question) mappedElements.get(question.getComparationId());
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setSubformat(AnswerSubformat subformat) {
		this.subformat = subformat;
	}

	public void setDatePeriodUnit(DatePeriodUnit datePeriodUnit) {
		this.datePeriodUnit = datePeriodUnit;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Compares two token ComparationValue. it must be of token between type.
	 */
	@Override
	public boolean isContentEqual(Token token) {
		if (token instanceof TokenBetween) {
			TokenComparationValue comparationValue = (TokenComparationValue) token;
			if (super.isContentEqual(token)) {
				if (!question.getPathName().equals(comparationValue.question.getPathName())) {
					return false;
				}
				
				if (subformat != comparationValue.subformat) {
					return false;
				}

				if (datePeriodUnit != null && datePeriodUnit != comparationValue.datePeriodUnit) {
					return false;
				}

				if (value != null && !value.equals(comparationValue.value)) {
					return false;
				}

				return true;
			}
		}
		return false;
	}
}
