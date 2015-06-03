package com.biit.webforms.persistence.entity;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerType;

@Entity
@Table(name = "tree_dynamic_questions")
@Cacheable(true)
public class DynamicQuestion extends WebformsBaseQuestion implements FlowConditionScript{
	private static final long serialVersionUID = -1233082747412521896L;
	
	private boolean mandatory;
	private boolean horizontal;
	
	private AnswerType type;

	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="dynamic_question_references")
	private List<TreeObject> references;
	
	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public AnswerType getType() {
		return type;
	}

	public void setType(AnswerType type) throws InvalidAnswerType {
		if(type != AnswerType.MULTIPLE_SELECTION || type != AnswerType.SINGLE_SELECTION_LIST || type != AnswerType.SINGLE_SELECTION_RADIO){
			throw new InvalidAnswerType();
		}
		this.type = type;
	}

	public List<TreeObject> getReferences() {
		return references;
	}

	public void setReferences(List<TreeObject> references) {
		this.references.clear();
		this.references.addAll(references);
	}

	@Override
	public String getScriptRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof DynamicQuestion) {
			copyBasicInfo(object);

			DynamicQuestion dynamicQuestion =(DynamicQuestion)object;
			setMandatory(dynamicQuestion.isMandatory());
			setHorizontal(dynamicQuestion.isHorizontal());
			try {
				setType(dynamicQuestion.getType());
			} catch (InvalidAnswerType e) {
				// impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			setReferences(dynamicQuestion.getReferences());
			
		} else {
			throw new NotValidTreeObjectException("Copy data for Question only supports the same type copy");
		}
	}

}
