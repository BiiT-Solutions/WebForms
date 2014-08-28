package com.biit.webforms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;

@Entity
@Table(name = "questions")
public class Question extends BaseQuestion {
	public static final int MAX_DESCRIPTION_LENGTH = 10000;
	public static final boolean DEFAULT_HORIZONTAL = false;
	public static final boolean DEFAULT_MANDATORY = true;

	private boolean mandatory;
	private boolean horizontal;

	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	@Enumerated(EnumType.STRING)
	private AnswerType answerType;
	@Enumerated(EnumType.STRING)
	private AnswerFormat answerFormat;

	public Question() {
		super();

		mandatory = DEFAULT_MANDATORY;
		horizontal = DEFAULT_HORIZONTAL;
		description = new String();
		answerType = AnswerType.INPUT;
		answerFormat = AnswerFormat.TEXT;
	}

	public Question(String name) throws FieldTooLongException {
		super(name);
	}

	public AnswerType getAnswerType() {
		return this.answerType;
	}

	/**
	 * This setter sets AnswerType to specified format and resets the answer
	 * format to Text or null.
	 * 
	 * @param answerType
	 */
	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
		try {
			if (answerType.isInputField()) {
				//If user sets type as TEXT then extra configuration values return to default
				setAnswerFormat(AnswerFormat.TEXT);
				horizontal = DEFAULT_HORIZONTAL;
				getChildren().clear();
			} else {
				//If user sets type as NOT TEXT then answer format becomes null
				setAnswerFormat(null);
			}
		} catch (InvalidAnswerFormatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public AnswerFormat getAnswerFormat() {
		return this.answerFormat;
	}

	public void setAnswerFormat(AnswerFormat answerFormat) throws InvalidAnswerFormatException {
		if (answerType.isInputField()) {
			if (answerFormat == null) {
				throw new InvalidAnswerFormatException("Input fields must define an answer format.");
			}
		} else {
			if (answerFormat != null) {
				throw new InvalidAnswerFormatException("Non Input fields cannot define an answer format.");
			}
		}
		this.answerFormat = answerFormat;
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		if (object instanceof Question) {			
			Question question = (Question) object;

			setDescription(new String(question.getDescription()));
			setHorizontal(question.isHorizontal());
			setMandatory(question.isMandatory());
			setAnswerType(question.getAnswerType());
			try {
				setAnswerFormat(question.getAnswerFormat());
			} catch (InvalidAnswerFormatException e) {
				// Its a copy, it should never happen.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			throw new NotValidTreeObjectException("Copy data for Question only supports the same type copy");
		}
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean isAllowedChildren(Class<? extends TreeObject> childClass){
		if(super.isAllowedChildren(childClass)){
			if(answerType == AnswerType.MULTIPLE_SELECTION || answerType == AnswerType.SINGLE_SELECTION){
				return true;
			}
		}
		return false;
	}
}
