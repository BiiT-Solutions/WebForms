package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseCategory;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;

@Entity
@Table(name = "subcategories")
public class Subcategory extends BaseGroup {
	private static final String DEFAULT_SUBCATEGORY_NAME = "Subcategory";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(BaseQuestion.class,
			BaseRepeatableGroup.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(BaseCategory.class));

	@Override
	public void checkDependencies() throws DependencyExistException {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<Class<?>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
	}

	@Override
	protected String getTechnicalName() {
		return DEFAULT_SUBCATEGORY_NAME;
	}

	@Override
	public void copyData(TreeObject object) {
		//Nothing to copy
	}

}
