package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "groups")
public class Group extends BaseRepeatableGroup {
	private static final boolean DEFAULT_REPEATABLE = false;

	public Group() {
		super();
		setRepeatable(DEFAULT_REPEATABLE);
	}

	public Group(String name) throws FieldTooLongException {
		super(name);
		setRepeatable(DEFAULT_REPEATABLE);
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		if (object instanceof Group) {			
			setRepeatable(((Group) object).isRepeatable());
		} else {
			throw new NotValidTreeObjectException("Copy data for Group only supports the same type copy");
		}
	}
}
