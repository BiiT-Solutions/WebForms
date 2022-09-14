package com.biit.webforms.persistence.entity;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.persistence.entity.exceptions.OnlyOneChildIsAllowedException;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tree_blocks")
@Cacheable()
public class Block extends Form implements IWebformsBlockView {
	private static final long serialVersionUID = -5029214862461479704L;
	private static final String DEFAULT_LABEL = "Block";
	public static final String DEFAULT_TECHNICAL_NAME = "block";

	public Block() {
		super();
	}

	@Override
	protected String getDefaultLabel() {
		return DEFAULT_LABEL;
	}

	public Block(String name, IUser<Long> user, Long organizationId) throws FieldTooLongException, CharacterNotAllowedException {
		super(name, user, organizationId);
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_TECHNICAL_NAME;
	}

	@Override
	public void addChild(TreeObject child) throws NotValidChildException, ElementIsReadOnly {
		if (!getChildren().isEmpty()) {
			throw new OnlyOneChildIsAllowedException("Building blocks only can have one category. ");
		}
		super.addChild(child);
	}

}
