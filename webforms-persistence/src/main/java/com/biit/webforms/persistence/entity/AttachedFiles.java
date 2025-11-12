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
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.serialization.AttachedFilesDeserializer;
import com.biit.webforms.serialization.AttachedFilesSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonDeserialize(using = AttachedFilesDeserializer.class)
@JsonSerialize(using = AttachedFilesSerializer.class)
@Table(name = "tree_attached_files")
@Cacheable
public class AttachedFiles extends WebformsBaseQuestion implements ElementWithDescription {
	private static final long serialVersionUID = 275419152896656379L;
	public static final int MAX_DESCRIPTION_LENGTH = 10000;
	public static final boolean DEFAULT_MANDATORY = true;

	private boolean mandatory;

	// Disables in orbeon the edition of this field. Means that when creating a
	// new form in orbeon is enabled, but when editing is disabled.
	@Column(name = "edition_disabled")
	private boolean editionDisabled = false;

	@Column(length = MAX_DESCRIPTION_LENGTH, columnDefinition = "TEXT")
	private String description = "";

	public AttachedFiles() {
		super();
		mandatory = DEFAULT_MANDATORY;
	}

	@Override
	public AnswerFormat getAnswerFormat() {
		return null;
	}

	@Override
	public AnswerSubformat getAnswerSubformat() {
		return null;
	}

	@Override
	public AnswerType getAnswerType() {
		return null;
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof AttachedFiles) {
			copyBasicInfo(object);

			AttachedFiles attachedFile = (AttachedFiles) object;

			setMandatory(attachedFile.isMandatory());
			setEditionDisabled(attachedFile.isEditionDisabled());
		} else {
			throw new NotValidTreeObjectException("Copy data for Attached Files only supports the same type copy");
		}

	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;

		sb.append("AttachedFiles ").append(idName).append("  = new AttachedFiles();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setMandatory(\"").append(this.isMandatory()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setEditionDisabled(\"").append(this.isEditionDisabled()).append("\");").append(System.lineSeparator());

		return counter;
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return new ArrayList<Class<? extends TreeObject>>();
	}

	public boolean isEditionDisabled() {
		return editionDisabled;
	}

	public void setEditionDisabled(boolean editionDisabled) {
		this.editionDisabled = editionDisabled;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

}
