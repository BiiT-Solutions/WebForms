package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.form.TreeObject;
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
public class Question extends WebformsBaseQuestion implements FlowConditionScript {
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
	}

	public AnswerType getAnswerType() {
		return this.answerType;
	}

	/**
	 * This setter sets AnswerType and sets the answer format to the default
	 * answer format for a type.
	 * 
	 * @param answerType
	 */
	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
		try {
			setAnswerFormat(answerType.getDefaultAnswerFormat());
			if (!answerType.isChildrenAllowed()) {
				getChildren().clear();
			}
			horizontal = answerType.getDefaultHorizontal();
			mandatory = answerType.getDefaultMandatory();
		} catch (InvalidAnswerFormatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public AnswerFormat getAnswerFormat() {
		return this.answerFormat;
	}

	/**
	 * Setter of answer format. Sets the answer format and sets the default
	 * answer subformat.
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
			this.answerSubformat = answerFormat.getDefaultSubformat();
		} else {
			this.answerSubformat = null;
		}
	}

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
			setMandatory(question.isMandatory());
			// This need to be set on order or otherwise the assignment process
			// will fail
			setAnswerType(question.getAnswerType());
			try {
				setAnswerFormat(question.getAnswerFormat());
			} catch (InvalidAnswerFormatException e) {
				// Its a copy, it should never happen.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			setHorizontal(question.isHorizontal());
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
}
