package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_categories")
@Cacheable(true)
public class Category extends BaseCategory implements ElementWithImage {
	private static final long serialVersionUID = 7418748035993485582L;
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(Arrays.asList(BaseQuestion.class,
			BaseRepeatableGroup.class));

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private TreeObjectImage image;

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
			int tempCounter = currentCounter + 1;
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

	/**
	 * This function checks if the content of two category elements is the same.
	 * 
	 * @param category
	 * @return
	 */

	@Override
	public boolean isContentEqual(TreeObject treeObject) {
		if (treeObject instanceof Category) {
			return super.isContentEqual(treeObject);
		}
		return false;
	}

	@Override
	public void setImage(TreeObjectImage image) {
		this.image = image;
	}

	@Override
	public TreeObjectImage getImage() {
		return image;
	}
}
