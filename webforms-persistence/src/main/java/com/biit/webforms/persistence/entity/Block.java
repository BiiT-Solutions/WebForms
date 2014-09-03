package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

@Entity
@Table(name = "blocks")
public class Block extends Form {

	public Block() {
		super();
	}

	public Block(String name, User user, Organization organization) throws FieldTooLongException {
		super(name, user, organization);
	}

}
