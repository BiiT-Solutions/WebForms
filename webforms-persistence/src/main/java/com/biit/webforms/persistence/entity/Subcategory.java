package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "subcategories")
public class Subcategory extends BaseGroup {
	private static final String DEFAULT_SUBCATEGORY_NAME = "Subcategory";
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(
			Arrays.asList(BaseQuestion.class, BaseRepeatableGroup.class));
	
	public Subcategory() {
		super();
	}

	public Subcategory(String name) throws FieldTooLongException {
		super();
		setName(name);
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected String getTechnicalName() {
		return DEFAULT_SUBCATEGORY_NAME;
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		if (object instanceof Subcategory) {
			//Nothing to copy
		} else {
			throw new NotValidTreeObjectException("Copy data for Subcategory only supports the same type copy");
		}
	}

}
