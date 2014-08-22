package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;

@Entity
@Table(name = "texts")
public class Text extends BaseQuestion {

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return new ArrayList<Class<? extends TreeObject>>();
	}

	@Override
	protected void copyData(TreeObject object) {
		// TODO Auto-generated method stub

	}

}
