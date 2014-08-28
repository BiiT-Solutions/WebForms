package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;

@Entity
@Table(name = "texts")
public class Text extends BaseQuestion {
	public static final int MAX_DESCRIPTION_LENGTH = 10000;
	public static final int MAX_DESCRIPTION_LABEL = 30;

	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	public Text() {
		super();
		description = new String();
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return new ArrayList<Class<? extends TreeObject>>();
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		if (object instanceof Text) {
			description = new String(((Text) object).getDescription());
		} else {
			throw new NotValidTreeObjectException("Copy data for Text only supports the same type copy");
		}
	}

	@Override
	public String getLabel() {
		if (description.length() > MAX_DESCRIPTION_LABEL) {
			return description.substring(0, MAX_DESCRIPTION_LABEL) + "...";
		}
		return description;
	}

	@Override
	public void setLabel(String label) {
		// Ignore
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
