package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_groups")
public class Group extends BaseRepeatableGroup {
	private static final boolean DEFAULT_REPEATABLE = false;

	public Group() {
		super();
		setRepeatable(DEFAULT_REPEATABLE);
	}

	public Group(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
		setRepeatable(DEFAULT_REPEATABLE);
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;
		
		sb.append("Group ").append(idName).append("  = new Group();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());
		if (isRepeatable()) {
			sb.append(idName).append(".setRepeatable(true);").append(System.lineSeparator());
		} else {
			sb.append(idName).append(".setRepeatable(false);").append(System.lineSeparator());
		}

		int currentCounter = counter;
		for (TreeObject child : getChildren()) {
			int tempCounter = currentCounter+1;
			if (child instanceof Group) {
				currentCounter = ((Group) child).exportToJavaCode(sb, currentCounter + 1);
			}
			if (child instanceof Question) {
				currentCounter = ((Question) child).exportToJavaCode(sb, currentCounter + 1);
			}
			if (child instanceof Text) {
				currentCounter = ((Text) child).exportToJavaCode(sb, currentCounter + 1);
			}
			if (child instanceof SystemField) {
				currentCounter = ((SystemField) child).exportToJavaCode(sb, currentCounter + 1);
			}
			sb.append("//group").append(System.lineSeparator());
			sb.append(idName).append(".addChild(").append("el_" + tempCounter).append(");").append(System.lineSeparator());
		}
		return currentCounter;
	}
}
