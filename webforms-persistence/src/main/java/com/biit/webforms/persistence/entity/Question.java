package com.biit.webforms.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Question extends WebformsBaseQuestion implements FlowConditionScript, ElementWithImage {
	private static final long serialVersionUID = -7243001035969348318L;
	public static final int MAX_DESCRIPTION_LENGTH = 10000;
	public static final int MAX_DEFAULT_VALUE = 10000;
	public static final boolean DEFAULT_HORIZONTAL = false;
	public static final boolean DEFAULT_MANDATORY = true;

	private boolean mandatory;
	private boolean horizontal;

	@Column(length = MAX_DESCRIPTION_LENGTH, columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "answer_type")
	private AnswerType answerType;

	@Enumerated(EnumType.STRING)
	@Column(name = "answer_format")
	private AnswerFormat answerFormat;

	@Enumerated(EnumType.STRING)
	@Column(name = "answer_subformat")
	private AnswerSubformat answerSubformat;

	@Column(name = "default_value_string", length = MAX_DESCRIPTION_LENGTH, columnDefinition = "TEXT")
	private String defaultValueString;

	@ManyToOne(optional = true)
	@JoinColumn(name = "default_value_answer")
	private Answer defaultValueAnswer;

	// Disables in orbeon the edition of this field. Means that when creating a
	// new form in orbeon is enabled, but when editing is disabled.
	@Column(name = "edition_disabled")
	private boolean editionDisabled = false;

	@Column(name = "default_value_time")
	private Timestamp defaultValueTime;

	@OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private TreeObjectImage image;

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

	@Override
	public void resetIds() {
		super.resetIds();
		if (image != null) {
			image.resetIds();
		}
	}

	@Override
	protected void resetDatabaseIds() {
		super.resetDatabaseIds();
		if (image != null) {
			image.resetDatabaseIds();
		}
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
	 * Setter of answer format. Sets the answer format and sets the default
	 * answer subformat.
	 * 
	 * @param answerFormat
	 * @throws InvalidAnswerFormatException
	 */
	public void setAnswerFormat(AnswerFormat answerFormat) throws InvalidAnswerFormatException {
		if (Objects.equals(answerType, AnswerType.INPUT)) {
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
			if (answerSubformat == null || (answerSubformat.getAnswerFormat() != null && !answerSubformat.getAnswerFormat().equals(answerFormat))) {
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
			throw new InvalidAnswerSubformatException("Answer subformat can't be defined if the question doesn't have any format.");
		}
		if (answerFormat != null && answerSubformat != null && !answerFormat.isSubformat(answerSubformat)) {
			throw new InvalidAnswerSubformatException("Answer subformat " + answerSubformat + " is not compatible with answer format " + answerFormat);
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

			setEditionDisabled(question.isEditionDisabled());

			setDefaultValueString(question.getDefaultValueString());
			setDefaultValueTime(question.getDefaultValueTime());
			if (question.getDefaultValueAnswer() != null) {
				setDefaultValueAnswer(getAnswer(question.getDefaultValueAnswer().getValue()));
			}
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
			sb.append(idName).append(".setAnswerType(AnswerType." + answerType + ");").append(System.lineSeparator());
			sb.append(idName).append(".setAnswerFormat(AnswerFormat." + answerFormat + ");").append(System.lineSeparator());
			sb.append(idName).append(".setAnswerSubformat(AnswerSubformat." + answerSubformat + ");").append(System.lineSeparator());
			break;
		default:
			sb.append(idName).append(".setAnswerType(AnswerType." + answerType + ");").append(System.lineSeparator());
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
			sb.append(idName).append(".addChild(").append("el_" + tempCounter).append(");").append(System.lineSeparator());
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
				if (this.isEditionDisabled() != question.isEditionDisabled()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public String getDefaultValueString() {
		return defaultValueString;
	}

	public void setDefaultValueString(String defaultValueString) {
		this.defaultValueString = defaultValueString;
	}

	public Answer getDefaultValueAnswer() {
		return defaultValueAnswer;
	}

	public void setDefaultValueAnswer(Answer defaultValueAnswer) {
		this.defaultValueAnswer = defaultValueAnswer;
	}

	public Timestamp getDefaultValueTime() {
		return defaultValueTime;
	}

	public void setDefaultValueTime(Timestamp defaultValueTime) {
		this.defaultValueTime = defaultValueTime;
	}

	/**
	 * Returns the default value as string. If no default value is defined
	 * returns empty string.
	 * 
	 * @return
	 */
	public String getDefaultValue() {
		if (getDefaultValueString() != null) {
			return getDefaultValueString();
		}
		if (getDefaultValueAnswer() != null) {
			return getDefaultValueAnswer().toString();
		}
		if (getDefaultValueTime() != null) {
			return getDefaultValueTime().toString();
		}
		return "";
	}

	public void setDefaultValue(Object defaultValue) {
		if (defaultValue == null) {
			setDefaultValueString(null);
			setDefaultValueTime(null);
			setDefaultValueAnswer(null);
		}
		if (defaultValue instanceof String) {
			setDefaultValueString((String) defaultValue);
			setDefaultValueTime(null);
			setDefaultValueAnswer(null);
			return;
		}
		if (defaultValue instanceof Timestamp) {
			setDefaultValueString(null);
			setDefaultValueTime((Timestamp) defaultValue);
			setDefaultValueAnswer(null);
			return;
		}
		if (defaultValue instanceof Date) {
			setDefaultValueString(null);
			setDefaultValueTime(new Timestamp(((Date) defaultValue).getTime()));
			setDefaultValueAnswer(null);
			return;
		}
		if (defaultValue instanceof Answer) {
			setDefaultValueString(null);
			setDefaultValueTime(null);
			setDefaultValueAnswer((Answer) defaultValue);
			return;
		}
	}

	public boolean isEditionDisabled() {
		return editionDisabled;
	}

	public void setEditionDisabled(boolean editionDisabled) {
		this.editionDisabled = editionDisabled;
	}

	@Override
	public void setImage(TreeObjectImage image) {
		this.image = image;
		if (image != null) {
			image.setElement(this);
		}
	}

	@Override
	public TreeObjectImage getImage() {
		return image;
	}
}
