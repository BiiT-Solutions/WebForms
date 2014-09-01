package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;

@Entity
@Table(name = "system_fields")
public class SystemField extends BaseQuestion {
	
	public String getLabel(){
		return "<"+ super.getLabel() + ">";
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		//Nothing to copy.
	}

	public void setFieldName(String value) {
		super.setLabel(value);
	}
	
	public String getFieldName() {
		return super.getLabel();
	}

}
