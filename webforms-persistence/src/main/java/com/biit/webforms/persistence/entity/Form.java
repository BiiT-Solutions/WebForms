package com.biit.webforms.persistence.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseForm;
import com.biit.form.exceptions.FieldTooLongException;

@Entity
@Table(name = "TREE_FORMS")
public class Form extends BaseForm {

	@Column(nullable = false)
	private Timestamp availableFrom;
	private Timestamp availableTo;

	public Form() {
		super();
	}

	public Form(String name) throws FieldTooLongException {
		super(name);
	}

	@Override
	public void resetIds() {
		super.resetIds();
	}

	@Override
	public void setCreationTime(Timestamp dateCreated) {
		if (this.availableFrom == null) {
			this.availableFrom = dateCreated;
		}
		super.setCreationTime(dateCreated);
	}

	public Timestamp getAvailableFrom() {
		return this.availableFrom;
	}

	public void setAvailableFrom(Timestamp availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Timestamp getAvailableTo() {
		return this.availableTo;
	}

	public void setAvailableTo(Timestamp availableTo) {
		this.availableTo = availableTo;
	}
}
