package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;

/**
 * Answer is a class that contains the information of a defined and possible
 * answer to a multiple choice question.
 * 
 * -Has the next properties: name (value for client purposes, the method get/set
 * name and value affect the same parameter)
 * 
 * -label
 * 
 * -description
 * 
 * 
 */
@Entity
@Table(name = "tree_answers")
@Cacheable(true)
public class Answer extends BaseAnswer implements FlowConditionScript, ElementWithImage {
	private static final long serialVersionUID = 7614678800982506178L;
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<Class<? extends TreeObject>>(Arrays.asList(Answer.class));
	public static final int MAX_DESCRIPTION_LENGTH = 10000;

	@Column(length = MAX_DESCRIPTION_LENGTH, columnDefinition = "varchar(" + MAX_DESCRIPTION_LENGTH + ")")
	private String description;

	@OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private TreeObjectImage image;

	public Answer() {
		super();
		description = new String();
	}

	public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		setValue(name);
		description = new String();
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (image != null) {
			image.resetIds();
		}
	}

	@Override
	protected void resetDatabaseIds() {
		super.resetDatabaseIds();
		if (image != null) {
			image.resetDatabaseIds();
		}
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDREN;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Answer) {
			copyBasicInfo(object);
			if (((Answer) object).getDescription() != null) {
				description = new String(((Answer) object).getDescription());
			}
		} else {
			throw new NotValidTreeObjectException("Copy data for Answer only supports the same type copy");
		}
	}

	/**
	 * Set value is an alias for {@link Answer#setLabel(String)}
	 * 
	 * @param value
	 * @throws FieldTooLongException
	 * @throws CharacterNotAllowedException
	 */
	public void setValue(String value) throws FieldTooLongException, CharacterNotAllowedException {
		setName(value);
	}

	/**
	 * Get Value is an alias for {@link Answer#getName()}
	 * 
	 * @return
	 */
	public String getValue() {
		return getName();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getScriptRepresentation() {
		return getScriptValueRepresentation(getName());
	}

	public static String getScriptValueRepresentation(String value) {
		return "'" + value + "'";
	}

	/**
	 * Checks if this answer is a subanswer by looking if it has a parent and if
	 * it has if is an answer.
	 * 
	 * @return
	 */
	public boolean isSubanswer() {
		if (getParent() == null || !(getParent() instanceof Answer)) {
			return false;
		}
		return true;
	}

	public String getPathAnswerValue() {
		if (getParent() == null || !(getParent() instanceof Answer)) {
			return getValue();
		} else {
			return getParent().getPathName() + TreeObject.DEFAULT_PATH_SEPARATOR + getValue();
		}
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * A final answer is an answer that doesn't contain childs
	 * 
	 * @return
	 */
	public boolean isFinalAnswer() {
		return getChildren().isEmpty();
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;

		sb.append("Answer ").append(idName).append("  = new Answer();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());

		return counter;
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		Form form = (Form) this.getAncestor(Form.class);
		if (form == null) {
			return;
		}

		for (Flow flow : form.getFlows()) {
			if (flow.isDependent(this)) {
				throw new DependencyExistException("Flow '" + flow + "' depends of element '" + this + "'");
			}
		}
	}

	@Override
	public void setImage(TreeObjectImage image) {
		this.image = image;
		if (image != null) {
			image.setElement(this);
		}
	}

	@Override
	public TreeObjectImage getImage() {
		return image;
	}
}
