package com.biit.webforms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.enumerations.FormWorkStatus;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

@Entity
@Table(name = "forms", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "version", "organizationId" }) })
public class Form extends BaseForm {
	public static final int MAX_DESCRIPTION_LENGTH = 30000;
	
	@Enumerated(EnumType.STRING)
	private FormWorkStatus status;

	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	private Long organizationId;

	public Form() {
		super();
		status = FormWorkStatus.DESIGN;
		description = new String();
	}

	public Form(String name, User user, Organization organization) throws FieldTooLongException {
		super(name);
		status = FormWorkStatus.DESIGN;
		description = new String();
		setCreatedBy(user);
		setUpdatedBy(user);
		setOrganizationId(organization);
	}

	@Override
	public void resetIds() {
		super.resetIds();
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		super.copyData(object);
		if (object instanceof Form) {
			description = new String(((Form) object).getDescription());
			organizationId = new Long(((Form) object).getOrganizationId());
			status = ((Form) object).getStatus();
		} else {
			throw new NotValidTreeObjectException("Copy data for Form only supports the same type copy");
		}
	}

	public Form createNewVersion(User user) throws NotValidTreeObjectException {
		Form newVersion = (Form) generateCopy(false, true);
		newVersion.setVersion(newVersion.getVersion() + 1);
		newVersion.resetIds();
		newVersion.setCreatedBy(user);
		newVersion.setUpdatedBy(user);
		newVersion.setStatus(FormWorkStatus.DESIGN);
		return newVersion;
	}

	public void setDescription(String description) throws FieldTooLongException {
		if (description.length() > MAX_DESCRIPTION_LENGTH) {
			throw new FieldTooLongException("Description is longer than maximum: " + MAX_DESCRIPTION_LENGTH);
		}
		this.description = new String(description);
	}

	public String getDescription() {
		return description;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Organization organization) {
		this.organizationId = organization.getOrganizationId();
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public FormWorkStatus getStatus() {
		return status;
	}

	public void setStatus(FormWorkStatus status) {
		this.status = status;
	}
}
