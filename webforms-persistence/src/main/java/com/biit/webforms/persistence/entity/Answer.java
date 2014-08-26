package com.biit.webforms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;

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
@Table(name = "answers")
public class Answer extends BaseAnswer {
	public static final int MAX_DESCRIPTION_LENGTH = 10000;

	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	public Answer() {
		super();
		description = new String();
	}

	public Answer(String name) throws FieldTooLongException {
		super(name);
	}

	@Override
	protected void copyData(TreeObject object) {
		// Nothing to copy
	}

	/**
	 * Set value is an alias for {@link Answer#setName(String)}
	 * 
	 * @param value
	 * @throws FieldTooLongException
	 */
	public void setValue(String value) throws FieldTooLongException {
		setName(value);
	}

	/**
	 * Get Value is an alias for {@link Answer#getName()}
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

}
