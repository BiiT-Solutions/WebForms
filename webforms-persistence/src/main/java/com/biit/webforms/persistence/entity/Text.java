package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;

@Entity
@Table(name = "texts")
public class Text extends BaseQuestion {
	public static final int MAX_DESCRIPTION_LENGTH = 10000;
	
	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return new ArrayList<Class<? extends TreeObject>>();
	}

	@Override
	protected void copyData(TreeObject object) {
		// TODO Auto-generated method stub

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
