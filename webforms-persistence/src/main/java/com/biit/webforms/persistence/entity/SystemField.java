package com.biit.webforms.persistence.entity;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.serialization.SystemFieldDeserializer;
import com.biit.webforms.serialization.SystemFieldSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonDeserialize(using = SystemFieldDeserializer.class)
@JsonSerialize(using = SystemFieldSerializer.class)
@Table(name = "tree_system_fields")
@Cacheable
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
		sb.append(idName).append(".setFieldName(\"").append(this.getFieldName()).append("\");").append(System.lineSeparator());

		return counter;
	}

	/**
	 * Compares the content of treeObject - Needs to be an instance of
	 * SystemField
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
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return new ArrayList<>();
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
