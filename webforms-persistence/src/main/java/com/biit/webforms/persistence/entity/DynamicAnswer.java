package com.biit.webforms.persistence.entity;

import java.util.regex.Pattern;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;

@Entity
@Table(name = "tree_dynamic_answer")
@Cacheable(true)
public class DynamicAnswer extends BaseAnswer implements FlowConditionScript {
	private static final long serialVersionUID = -1233082747412521896L;
	public static final String ANSWER_TAG_ALLOWED_CHARS = ".*";
	private static final Pattern NAME_ALLOWED = Pattern.compile(ANSWER_TAG_ALLOWED_CHARS);

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
	private Question reference;

	public Question getReference() {
		return reference;
	}

	public void setReference(Question reference) {
		this.reference = reference;
	}

	public void setName(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super.setName(getComparationId());
	}

	public String getReferenceName() {
		if (reference != null) {
			return "{" + reference.getPathName() + "}";
		} else {
			return "{}";
		}
	}

	@Override
	public String getScriptRepresentation() {
		return null;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DynamicAnswer) {
			copyBasicInfo(object);

			DynamicAnswer dynamicQuestion = (DynamicAnswer) object;
			setReference(dynamicQuestion.getReference());
		} else {
			throw new NotValidTreeObjectException("Copy data for Question only supports the same type copy");
		}
	}

	@Override
	public Pattern getNameAllowedPattern() {
		return NAME_ALLOWED;
	}
}
