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
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Answer is a class that contains the information of a defined and possible
 * answer to a multiple choice question.
 * 
 * -Has the next properties: name (value for client purposes, the method get/set
 * name and value affect the same parameter)
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

	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	public Answer() {
		super();
		description = new String();
	}

	public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		description = new String();
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDREN;
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		if (object instanceof Answer) {
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
		return "'" + getName() + "'";
	}

	/**
	 * Checks if this answer is a subanswer by looking if it has a parent and if
	 * it has if is an answer.
	 * 
	 * @return
	 */
	public boolean isSubanswer() {
		if (getParent() == null || !(getParent() instanceof Answer)) {
			return false;
		}
		return true;
	}
}
