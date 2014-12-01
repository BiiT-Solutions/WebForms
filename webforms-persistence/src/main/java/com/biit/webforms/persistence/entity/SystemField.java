package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_system_fields")
public class SystemField extends WebformsBaseQuestion {

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

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;
		
		sb.append("SystemField ").append(idName).append("  = new SystemField();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setFieldName(\"").append(this.getFieldName()).append("\");").append(System.lineSeparator());

		return counter;
	}

}
