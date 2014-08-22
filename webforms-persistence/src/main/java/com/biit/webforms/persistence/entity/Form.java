package com.biit.webforms.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;
import com.liferay.portal.model.User;

@Entity
@Table(name = "forms")
public class Form extends BaseForm {
	public static final int MAX_DESCRIPTION_LENGTH = 30000;
	
	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;

	public Form() {
		super();
	}

	public Form(String name, User user) throws FieldTooLongException {
		super(name);
		setCreatedBy(user);
		setUpdatedBy(user);
	}

	@Override
	public void resetIds() {
		super.resetIds();
	}

	@Override
	protected void copyData(TreeObject object) {
		super.copyData(object);
		Form form = (Form) object;
		try {
			setDescription(form.getDescription());
		} catch (FieldTooLongException e) {
			e.printStackTrace();
		}
	}
	
	public Form createNewVersion(User user){
		Form newVersion = (Form) generateCopy(false, true);
		newVersion.setVersion(newVersion.getVersion()+1);
		newVersion.resetIds();
		newVersion.setCreatedBy(user);
		newVersion.setUpdatedBy(user);
		return newVersion;
	}
	
	public void setDescription(String description) throws FieldTooLongException{
		if(description.length()>MAX_DESCRIPTION_LENGTH){
			throw new FieldTooLongException("Description is longer than maximum: "+MAX_DESCRIPTION_LENGTH);
		}
		this.description = new String(description);
	}
	
	public String getDescription(){
		return description;
	}
}
