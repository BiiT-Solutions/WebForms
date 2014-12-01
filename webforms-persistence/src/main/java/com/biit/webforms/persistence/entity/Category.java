package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseCategory;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_categories")
public class Category extends BaseCategory {
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(
			Arrays.asList(BaseQuestion.class, BaseRepeatableGroup.class));

	public Category() {
		super();
	}

	public Category(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Category) {
			// Nothing to copy except basic information data.
			copyBasicInfo(object);
		} else {
			throw new NotValidTreeObjectException("Copy data for Category only supports the same type copy");
		}
	}

	public int exportToJavaCode(StringBuilder sb, int counter) {
		String idName = "el_" + counter;

		sb.append("Category ").append(idName).append("  = new Category();").append(System.lineSeparator());
		sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
		sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());
		
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
			
			sb.append("//cat").append(System.lineSeparator());
			sb.append(idName).append(".addChild(").append("el_" + tempCounter).append(");").append(System.lineSeparator());
		}
		return currentCounter;
	}
}
