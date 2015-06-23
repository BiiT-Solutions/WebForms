package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@Entity
@Table(name = "tree_questions")
@Cacheable(true)
public class Question extends WebformsBaseQuestion implements FlowConditionScript {
	private static final long serialVersionUID = -7243001035969348318L;
	public static final int MAX_DESCRIPTION_LENGTH = 10000;
	public static final boolean DEFAULT_HORIZONTAL = false;
	public static final boolean DEFAULT_MANDATORY = true;

	private boolean mandatory;
	private boolean horizontal;

	@Column(length = MAX_DESCRIPTION_LENGTH, columnDefinition = "varchar(" + MAX_DESCRIPTION_LENGTH + ")")
	private String description;

	@Enumerated(EnumType.STRING)
	private AnswerType answerType;
	@Enumerated(EnumType.STRING)
	private AnswerFormat answerFormat;
	@Enumerated(EnumType.STRING)
	private AnswerSubformat answerSubformat;

	public Question() {
		super();
		mandatory = DEFAULT_MANDATORY;
		horizontal = DEFAULT_HORIZONTAL;
		description = new String();
		answerType = AnswerType.INPUT;
		answerFormat = AnswerFormat.TEXT;
		answerSubformat = AnswerSubformat.TEXT;
	}

	public Question(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		mandatory = DEFAULT_MANDATORY;
		horizontal = DEFAULT_HORIZONTAL;
		description = new String();
		answerType = AnswerType.INPUT;
		answerFormat = AnswerFormat.TEXT;
		answerSubformat = AnswerSubformat.TEXT;
	}

	public AnswerType getAnswerType() {
		return this.answerType;
	}

	/**
	 * This setter sets AnswerType and sets the answer format to the default answer format for a type.
	 * 
	 * @param answerType
	 */
	public void setAnswerType(AnswerType answerType) {
		AnswerType prevValue = this.answerType;
		this.answerType = answerType;
		try {
			// If you change to input field, select the default value.
			if (answerType != prevValue) {
				setAnswerFormat(answerType.getDefaultAnswerFormat());
				if (!answerType.isChildrenAllowed()) {
					getChildren().clear();
				}
				// Dropdown list does not allow subanswers.
				if (!answerType.isSubChildrenAllowed()) {
					for (TreeObject child : getChildren()) {
						child.getChildren().clear();
					}
				}
			}
			if (!answerType.isHorizontalEnabled()) {
				horizontal = answerType.getDefaultHorizontal();
			}
			if (!answerType.isMandatoryEnabled()) {
				mandatory = answerType.getDefaultMandatory();
			}
		} catch (InvalidAnswerFormatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Override
	public AnswerFormat getAnswerFormat() {
		return this.answerFormat;
	}

	/**
	 * Setter of answer format. Sets the answer format and sets the default answer subformat.
	 * 
	 * @param answerFormat
	 * @throws InvalidAnswerFormatException
	 */
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
		if (answerFormat != null) {
			// Answer subform is not valid for answerSubformat, change it.
			if (answerSubformat == null
					|| (answerSubformat.getAnswerFormat() != null && !answerSubformat.getAnswerFormat().equals(
							answerFormat))) {
				this.answerSubformat = answerFormat.getDefaultSubformat();
			}
		} else {
			this.answerSubformat = null;
		}
	}

	@Override
	public AnswerSubformat getAnswerSubformat() {
		return answerSubformat;
	}

	public void setAnswerSubformat(AnswerSubformat answerSubformat) throws InvalidAnswerSubformatException {
		if (answerFormat == null && answerSubformat != null) {
			throw new InvalidAnswerSubformatException(
					"Answer subformat can't be defined if the question doesn't have any format.");
		}
		if (answerFormat != null && answerSubformat != null && !answerFormat.isSubformat(answerSubformat)) {
			throw new InvalidAnswerSubformatException("Answer subformat " + answerSubformat
					+ " is not compatible with answer format " + answerFormat);
		}
		if (answerFormat != null && answerSubformat == null) {
			this.answerSubformat = answerFormat.getDefaultSubformat();
		} else {
			this.answerSubformat = answerSubformat;
		}
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Question) {
			copyBasicInfo(object);

			Question question = (Question) object;

			setDescription(new String(question.getDescription()));
			// This need to be set on order or otherwise the assignment process
			// will fail
			setAnswerType(question.getAnswerType());
			try {
				setAnswerFormat(question.getAnswerFormat());
			} catch (InvalidAnswerFormatException e) {
				// Its a copy, it should never happen.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			try {
				setAnswerSubformat(question.getAnswerSubformat());
			} catch (InvalidAnswerSubformatException e) {
				// Its a copy, it should never happen.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			setMandatory(question.isMandatory());
			setHorizontal(question.isHorizontal());
		} else {
			throw new NotValidTreeObjectException("Copy data for Question only supports the same type copy");
		}
	}

	@Override
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
		if (description != null) {
			return description;
		} else {
			return new String();
		}
	}

	public void setDescription(String description) {
		if (description == null) {
			this.description = new String();
		} else {
			this.description = description;
		}
	}

	@Override
	public boolean isAllowedChildren(Class<? extends TreeObject> childClass) {
		if (super.isAllowedChildren(childClass)) {
			if (answerType.isChildrenAllowed()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getScriptRepresentation() {
		List<TreeObject> parents = new ArrayList<TreeObject>();
		TreeObject tempParent = getParent();
		while (tempParent != null && !(tempParent instanceof Form)) {
			parents.add(tempParent);
			tempParent = tempParent.getParent();
		}
		String representation = "<" + getName() + ">";
		for (int i = 0; i < parents.size(); i++) {
			representation = "<" + parents.get(i).getName() + ">" + representation;
		}
		return representation;
	}

	public LinkedHashSet<Answer> getFinalAnswers() {
		LinkedHashSet<TreeObject> childAnswers = getAllChildrenInHierarchy(Answer.class);
		LinkedHashSet<Answer> finalAnswers = new LinkedHashSet<>();
		for (TreeObject childAnswer : childAnswers) {
			Answer answer = (Answer) childAnswer;
			if (answer.getChildren() == null || answer.getChildren().isEmpty()) {
				finalAnswers.add(answer);
			}
		}
		return finalAnswers;
	}

	public boolean containsDynamicAnswer() {
		return !getAllChildrenInHierarchy(DynamicAnswer.class).isEmpty();
	}

	/**
	 * Returns the answer with value == @answerValue otherwise returns null.
	 * 
	 * @param answerValue
	 * @return
	 */
	public Answer getAnswer(String answerValue) {
		LinkedHashSet<Answer> answers = getFinalAnswers();
		for (Answer answer : answers) {
			if (answer.getValue().equals(answerValue)) {
				return answer;
			}
		}
		return null;
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;

		sb.append("Question ").append(idName).append("  = new Question();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());
		switch (answerType) {
		case INPUT:
			sb.append(idName).append(".setAnswerType(AnswerType.INPUT);").append(System.lineSeparator());
			switch (answerFormat) {
			case DATE:
				sb.append(idName).append(".setAnswerFormat(AnswerFormat.DATE);").append(System.lineSeparator());
				break;
			case NUMBER:
				sb.append(idName).append(".setAnswerFormat(AnswerFormat.NUMBER);").append(System.lineSeparator());
				break;
			case POSTAL_CODE:
				sb.append(idName).append(".setAnswerFormat(AnswerFormat.POSTAL_CODE);").append(System.lineSeparator());
				break;
			case TEXT:
				sb.append(idName).append(".setAnswerFormat(AnswerFormat.TEXT);").append(System.lineSeparator());
				break;
			}
			switch (answerSubformat) {
			case AMOUNT:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.AMOUNT);").append(System.lineSeparator());
				break;
			case BSN:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.BSN);").append(System.lineSeparator());
				break;
			case DATE:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.DATE);").append(System.lineSeparator());
				break;
			case DATE_BIRTHDAY:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.DATE_BIRTHDAY);")
						.append(System.lineSeparator());
				break;
			case DATE_FUTURE:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.DATE_FUTURE);")
						.append(System.lineSeparator());
				break;
			case DATE_PAST:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.DATE_PAST);")
						.append(System.lineSeparator());
				break;
			case DATE_PERIOD:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.DATE_PERIOD);")
						.append(System.lineSeparator());
				break;
			case EMAIL:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.EMAIL);").append(System.lineSeparator());
				break;
			case FLOAT:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.FLOAT);").append(System.lineSeparator());
				break;
			case IBAN:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.IBAN);").append(System.lineSeparator());
				break;
			case NUMBER:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.NUMBER);").append(System.lineSeparator());
				break;
			case PHONE:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.PHONE);").append(System.lineSeparator());
				break;
			case POSTAL_CODE:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.POSTAL_CODE);")
						.append(System.lineSeparator());
				break;
			case TEXT:
				sb.append(idName).append(".setAnswerSubformat(AnswerSubformat.TEXT);").append(System.lineSeparator());
				break;
			}
			break;
		case MULTIPLE_SELECTION:
			sb.append(idName).append(".setAnswerType(AnswerType.MULTIPLE_SELECTION);").append(System.lineSeparator());
			break;
		case SINGLE_SELECTION_LIST:
			sb.append(idName).append(".setAnswerType(AnswerType.SINGLE_SELECTION_LIST);")
					.append(System.lineSeparator());
			break;
		case SINGLE_SELECTION_RADIO:
			sb.append(idName).append(".setAnswerType(AnswerType.SINGLE_SELECTION_RADIO);")
					.append(System.lineSeparator());
			break;
		case TEXT_AREA:
			sb.append(idName).append(".setAnswerType(AnswerType.TEXT_AREA);").append(System.lineSeparator());
			break;
		}

		if (isHorizontal()) {
			sb.append(idName).append(".setHorizontal(true);").append(System.lineSeparator());
		} else {
			sb.append(idName).append(".setHorizontal(false);").append(System.lineSeparator());
		}
		if (isMandatory()) {
			sb.append(idName).append(".setMandatory(true);").append(System.lineSeparator());
		} else {
			sb.append(idName).append(".setMandatory(false);").append(System.lineSeparator());
		}

		int currentCounter = counter;
		for (TreeObject child : getChildren()) {
			int tempCounter = currentCounter + 1;
			currentCounter = ((Answer) child).exportToJavaCode(sb, currentCounter + 1);
			sb.append("//ques").append(System.lineSeparator());
			sb.append(idName).append(".addChild(").append("el_" + tempCounter).append(");")
					.append(System.lineSeparator());
		}

		return currentCounter;
	}

	/**
	 * Compares the content of treeObject - Needs to be an instance of Question
	 * 
	 * @param treeObject
	 * @return
	 */
	public boolean isContentEqual(TreeObject treeObject) {
		if (treeObject instanceof Question) {
			if (super.isContentEqual(treeObject)) {
				Question question = (Question) treeObject;
				if (this.getAnswerType() != question.getAnswerType()) {
					return false;
				}
				if (this.getAnswerFormat() != question.getAnswerFormat()) {
					return false;
				}
				if (this.getAnswerSubformat() != question.getAnswerSubformat()) {
					return false;
				}
				if (this.isMandatory() != question.isMandatory()) {
					return false;
				}
				if (this.isHorizontal() != question.isHorizontal()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}
}
