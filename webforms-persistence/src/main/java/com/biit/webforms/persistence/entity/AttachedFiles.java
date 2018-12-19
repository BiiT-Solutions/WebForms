package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;

@Entity
@Table(name = "tree_attached_files")
@Cacheable(true)
public class AttachedFiles extends WebformsBaseQuestion {
	private static final long serialVersionUID = 275419152896656379L;

	private boolean mandatory;

	// Disables in orbeon the edition of this field. Means that when creating a
	// new form in orbeon is enabled, but when editing is disabled.
	@Column(name = "edition_disabled")
	private boolean editionDisabled = false;

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

}
