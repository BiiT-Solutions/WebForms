package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_system_fields")
public class SystemField extends BaseQuestion {

	@Override
	public String getLabel() {
		return "<" + super.getLabel() + ">";
	}

	public void setFieldName(String value) throws FieldTooLongException {
		super.setLabel(value);
	}

	public String getFieldName() {
		return super.getLabel();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// Copy basic data
		copyBasicInfo(object);
	}

}
