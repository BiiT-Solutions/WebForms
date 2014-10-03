package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseCategory;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_categories")
public class Category extends BaseCategory {
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(Arrays.asList(BaseQuestion.class,
			BaseRepeatableGroup.class));

	public Category() {
		super();
	}

	public Category(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		if (object instanceof Category) {
			//Nothing to copy
		} else {
			throw new NotValidTreeObjectException("Copy data for Category only supports the same type copy");
		}
	}
}
