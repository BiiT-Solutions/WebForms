package com.biit.webforms.persistence.entity.condition;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Table(name = "token_comparation_value")
public class TokenComparationValue extends TokenWithQuestion implements ITokenQuestion {
	private static final long serialVersionUID = 8580195041605107217L;
	// Date format used to store in database
	public static final String DATE_FORMAT = "dd/MM/yyyy";

	private static TokenTypes tokenTypes[] = new TokenTypes[] { TokenTypes.EQ, TokenTypes.NE, TokenTypes.LT, TokenTypes.GT, TokenTypes.LE,
			TokenTypes.GE };

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AnswerSubformat subformat;

	@Enumerated(EnumType.STRING)
	@Column(name="date_period_unit")
	private DatePeriodUnit datePeriodUnit;

	private String value;

	// Evaluation value is false by default
	private transient Boolean evaluationValue = null;

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

	public void setContent(WebformsBaseQuestion reference, TokenTypes tokenType, AnswerSubformat subformat, DatePeriodUnit datePeriodUnit,
			String value) throws NotValidTokenType {
		setType(tokenType);
		setQuestion(reference);
		this.subformat = subformat;
		this.datePeriodUnit = datePeriodUnit;
		this.value = value;
	}

	public void setContent(TokenTypes tokenType, AnswerSubformat subformat, DatePeriodUnit datePeriodUnit, String value)
			throws NotValidTokenType {
		setContent(getQuestion(), tokenType, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getToken(TokenTypes tokenType, WebformsBaseQuestion reference, AnswerSubformat subformat,
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

	public static TokenComparationValue getTokenEqual(WebformsBaseQuestion reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.EQ, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenNotEqual(WebformsBaseQuestion reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.NE, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenLessThan(WebformsBaseQuestion reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.LT, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenGreaterThan(WebformsBaseQuestion reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.GT, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenLessEqual(WebformsBaseQuestion reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.LE, reference, subformat, datePeriodUnit, value);
	}

	public static TokenComparationValue getTokenGreaterEqual(WebformsBaseQuestion reference, AnswerSubformat subformat,
			DatePeriodUnit datePeriodUnit, String value) {
		return getToken(TokenTypes.GE, reference, subformat, datePeriodUnit, value);
	}

	@Override
	public String toString() {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getName();
		}

		if (subformat == AnswerSubformat.DATE_PERIOD) {
			return referenceString + " (" + datePeriodUnit + ")" + getType() + value.substring(0, value.length());
		}

		return referenceString + getType() + value;
	}

	public String getLocalizedString(String localizedDatePeriodUnit) {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getPathName();
		}

		if (subformat == AnswerSubformat.DATE_PERIOD) {
			return referenceString + " (" + localizedDatePeriodUnit + ")" + getType() + value.substring(0, value.length());
		}

		return referenceString + getType() + value;
	}

	@Override
	public String getExpressionSimplifierRepresentation() {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getPathName().replaceAll("[^A-Za-z0-9_./]", "_");
		}
		return referenceString + getType().getExpressionSimplifierRepresentation() + value;
	}

	@Override
	public String getExpressionEditorRepresentation() {
		String referenceString = null;
		if (getQuestion() != null) {
			referenceString = getQuestion().getPathName();
		}
		return referenceString + getType().getExpressionEditorRepresentation() + value;
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
			subformat = token.getSubformat();
			datePeriodUnit = token.getDatePeriodUnit();
			value = token.getValue();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenComparationValue.class.getName());
		}
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
		if (token instanceof TokenComparationValue) {
			TokenComparationValue comparationValue = (TokenComparationValue) token;
			if (super.isContentEqual(token)) {
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

	@Override
	public Boolean evaluate() {
		return evaluationValue;
	}

	public void evaluate(String value) {
		if(value == null || value.isEmpty()){
			evaluationValue = null;
			return;
		}
		
		evaluationValue = false;		
		switch (getQuestion().getAnswerFormat()) {
		case DATE:
			if (subformat == AnswerSubformat.DATE_PERIOD) {
				evaluationValue = evaluateAsDatePeriod(value);
			} else {
				evaluationValue = evaluateAsDate(value);
			}
			break;
		case NUMBER:
			evaluationValue = evaluateAsNumber(value);
			break;
		case POSTAL_CODE:
			evaluationValue = evaluateAsPostalCode(value);
			break;
		case TEXT:
			// No rules for text.
			break;
		}
	}

	private boolean evaluateAsPostalCode(String value) {
		switch (getType()) {
		case LT:
			return (this.value.compareTo(value) < 0);
		case LE:
			return (this.value.compareTo(value) <= 0);
		case GT:
			return (this.value.compareTo(value) > 0);
		case GE:
			return (this.value.compareTo(value) >= 0);
		case EQ:
			return Objects.equals(this.value, value);
		case NE:
			return !Objects.equals(this.value, value);
		default:
			throw new UnsupportedOperationException();
		}
	}

	private boolean evaluateAsNumber(String value) {
		double tokenValue = Double.parseDouble(this.value);
		double evaluationValue = Double.parseDouble(value);

		switch (getType()) {
		case LT:
			return evaluationValue < tokenValue;
		case LE:
			return evaluationValue <= tokenValue;
		case GT:
			return evaluationValue > tokenValue;
		case GE:
			return evaluationValue >= tokenValue;
		case EQ:
			return evaluationValue == tokenValue;
		case NE:
			return evaluationValue != tokenValue;
		default:
			throw new UnsupportedOperationException();
		}
	}

	private boolean evaluateAsDate(String value) {
		DateFormat format = new SimpleDateFormat(TokenComparationValue.DATE_FORMAT);
		try {
			Date condition = format.parse(getValue());
			Date userInput = format.parse(value);

			switch (getType()) {
			case LT:
				return (condition.compareTo(userInput) < 0);
			case LE:
				return (condition.compareTo(userInput) <= 0);
			case GT:
				return (condition.compareTo(userInput) > 0);
			case GE:
				return (condition.compareTo(userInput) >= 0);
			case EQ:
				return Objects.equals(condition, userInput);
			case NE:
				return !Objects.equals(condition, userInput);
			default:
				throw new UnsupportedOperationException();
			}
		} catch (ParseException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return false;
		}

	}

	private boolean evaluateAsDatePeriod(String value) {
		DateFormat format = new SimpleDateFormat(TokenComparationValue.DATE_FORMAT);
		try {
			Date userInput = format.parse(value);
			int delta = Integer.parseInt(getValue());

			Calendar calendar = Calendar.getInstance();

			int deltaSign = 0;
			if (subformat == AnswerSubformat.DATE_FUTURE) {
				// Date in the future
				deltaSign = 1;
			} else {
				// Date in the past
				deltaSign = -1;
			}
			switch (datePeriodUnit) {
			case DAY:
				calendar.add(Calendar.DAY_OF_MONTH, deltaSign * delta);
				break;
			case MONTH:
				calendar.add(Calendar.MONTH, deltaSign * delta);
				break;
			case YEAR:
				calendar.add(Calendar.YEAR, deltaSign * delta);
				break;
			}

			switch (getType()) {
			case LT:
				return (calendar.getTime().compareTo(userInput) < 0);
			case LE:
				return (calendar.getTime().compareTo(userInput) <= 0);
			case GT:
				return (calendar.getTime().compareTo(userInput) > 0);
			case GE:
				return (calendar.getTime().compareTo(userInput) >= 0);
			case EQ:
				return Objects.equals(calendar.getTime(), userInput);
			case NE:
				return !Objects.equals(calendar.getTime(), userInput);
			default:
				throw new UnsupportedOperationException();
			}
		} catch (ParseException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return false;
		}
	}
}
