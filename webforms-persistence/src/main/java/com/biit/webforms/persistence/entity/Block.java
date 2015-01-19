package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.liferay.portal.model.User;

@Entity
@Table(name = "tree_blocks")
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class Block extends Form implements IWebformsBlockView {
	private static final long serialVersionUID = -5029214862461479704L;

	public Block() {
		super();
	}

	public Block(String name, User user, Long organizationId) throws FieldTooLongException,
			CharacterNotAllowedException {
		super(name, user, organizationId);
	}

}
