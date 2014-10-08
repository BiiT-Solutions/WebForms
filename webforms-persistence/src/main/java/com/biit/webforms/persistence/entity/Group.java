package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_groups")
public class Group extends BaseRepeatableGroup {
	private static final boolean DEFAULT_REPEATABLE = false;

	public Group() {
		super();
		setRepeatable(DEFAULT_REPEATABLE);
	}

	public Group(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		setRepeatable(DEFAULT_REPEATABLE);
	}
}
