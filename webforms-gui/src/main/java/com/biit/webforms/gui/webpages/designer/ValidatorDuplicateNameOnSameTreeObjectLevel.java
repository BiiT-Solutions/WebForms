package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Validator;

public class ValidatorDuplicateNameOnSameTreeObjectLevel implements Validator {
	private static final long serialVersionUID = -6253757334392693487L;

	private TreeObject treeObject;

	public ValidatorDuplicateNameOnSameTreeObjectLevel(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String newName = (String) value;
		TreeObject parent = treeObject.getParent();

		if (parent != null) {
			validateNotDuplicatedName(parent, newName);
			// Checks also the form reference.
			if (parent instanceof Form) {
				if (((Form) parent).getFormReference() != null) {
					validateNotDuplicatedName(((Form) parent).getFormReference(), newName);
				}
			}

		}
	}

	private void validateNotDuplicatedName(TreeObject parent, String newName) throws InvalidValueException {
		if (parent != null) {
			for (TreeObject child : parent.getChildren()) {
				if (child.equals(treeObject) || child instanceof BlockReference) {
					// If this child is treeObject, ignore.
					continue;
				}
				if (child.getName().equals(newName)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME.translation());
				}
				if (child.getName().equalsIgnoreCase(newName)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_NAME_CASE.translation());
				}
			}
		}
	}

}
