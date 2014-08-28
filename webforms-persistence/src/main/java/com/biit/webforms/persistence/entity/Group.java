package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "groups")
public class Group extends BaseRepeatableGroup {

	public Group() {
		super();
	}

	public Group(String name) throws FieldTooLongException {
		super(name);
	}

	@Override
	protected void copyData(TreeObject object) {
		//Nothing to copy
	}
}
