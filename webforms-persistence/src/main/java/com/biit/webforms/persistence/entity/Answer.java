package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;

/**
 * Answer is a class that contains the information of a defined and possible answer to a multiple choice question.
 * 
 * -Has the next properties: name (value for client purposes, the method get/set name and value affect the same
 * parameter)
 * 
 * -label
 * 
 * -description
 * 
 * @author joriz_000
 * 
 */
@Entity
@Table(name = "tree_answers")
public class Answer extends BaseAnswer implements FlowConditionScript {
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<Class<? extends TreeObject>>(
			Arrays.asList(Answer.class));
	public static final int MAX_DESCRIPTION_LENGTH = 10000;

	@Column(length = MAX_DESCRIPTION_LENGTH, columnDefinition = "varchar(" + MAX_DESCRIPTION_LENGTH + ")")
	private String description;

	public Answer() {
		super();
		description = new String();
	}

	public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		setValue(name);
		description = new String();
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDREN;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Answer) {
			copyBasicInfo(object);
			if (((Answer) object).getDescription() != null) {
				description = new String(((Answer) object).getDescription());
			}
		} else {
			throw new NotValidTreeObjectException("Copy data for Answer only supports the same type copy");
		}
	}

	/**
	 * Set value is an alias for {@link Answer#setLabel(String)}
	 * 
	 * @param value
	 * @throws FieldTooLongException
	 * @throws CharacterNotAllowedException
	 */
	public void setValue(String value) throws FieldTooLongException, CharacterNotAllowedException {
		setName(value);
	}

	/**
	 * Get Value is an alias for {@link Answer#getName()}
	 * 
	 * @return
	 */
	public String getValue() {
		return getName();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getScriptRepresentation() {
		return getScriptValueRepresentation(getName());
	}

	public static String getScriptValueRepresentation(String value) {
		return "'" + value + "'";
	}

	/**
	 * Checks if this answer is a subanswer by looking if it has a parent and if it has if is an answer.
	 * 
	 * @return
	 */
	public boolean isSubanswer() {
		if (getParent() == null || !(getParent() instanceof Answer)) {
			return false;
		}
		return true;
	}

	public String getPathAnswerValue() {
		if (getParent() == null || !(getParent() instanceof Answer)) {
			return getValue();
		} else {
			return getParent().getPathName() + TreeObject.DEFAULT_PATH_SEPARATOR + getValue();
		}
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * A final answer is an answer that doesn't contain childs
	 * 
	 * @return
	 */
	public boolean isFinalAnswer() {
		return getChildren().isEmpty();
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;
		
		sb.append("Answer ").append(idName).append("  = new Answer();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());
		
		return counter;
	}
}
