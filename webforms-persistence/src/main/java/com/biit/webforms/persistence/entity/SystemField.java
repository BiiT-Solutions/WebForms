package com.biit.webforms.persistence.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.WebformsLogger;

@Entity
@Table(name = "tree_system_fields")
@Cacheable(true)
public class SystemField extends WebformsBaseQuestion {
	private static final long serialVersionUID = -5714738729351011443L;

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
		if (object instanceof SystemField) {
			// Copy basic data and fieldName
			copyBasicInfo(object);
			try {
				setFieldName(((SystemField) object).getFieldName());
			} catch (FieldTooLongException e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			throw new NotValidTreeObjectException("Copy data for SystemField only supports the same type copy");
		}
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;

		sb.append("SystemField ").append(idName).append("  = new SystemField();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setFieldName(\"").append(this.getFieldName()).append("\");")
				.append(System.lineSeparator());

		return counter;
	}

	/**
	 * Compares the content of treeObject - Needs to be an instance of SystemField
	 * 
	 * @param treeObject
	 * @return
	 */
	public boolean isContentEqual(TreeObject treeObject) {
		if (treeObject instanceof SystemField) {
			if (super.isContentEqual(treeObject)) {
				SystemField question = (SystemField) treeObject;
				if (this.getFieldName() != null && !this.getFieldName().equals(question.getFieldName())) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public AnswerFormat getAnswerFormat() {
		return AnswerFormat.TEXT;
	}

	@Override
	public AnswerSubformat getAnswerSubformat() {
		return AnswerSubformat.TEXT;
	}

	@Override
	public AnswerType getAnswerType() {
		return AnswerType.INPUT;
	}

	@Override
	public boolean isMandatory() {
		return false;
	}
}
