package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.vaadin.data.Validator;

public class ValidatorNestedTablesNotAllowed implements Validator {
	private static final long serialVersionUID = -5320539195645056658L;

	private TreeObject treeObject;

	public ValidatorNestedTablesNotAllowed(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Boolean isTable = (Boolean) value;
		if (isTable != null && isTable) {
			TreeObject parent = treeObject.getParent();

			if (parent != null) {
				validateNotNestedTables(parent, isTable);
			}
		}
	}

	private void validateNotNestedTables(TreeObject parent, Boolean isTable) throws InvalidValueException {
		if (parent != null) {
			if (parent instanceof Group) {
				if (((Group) parent).isShownAsTable() && isTable) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_NESTED_TABLE.translation());
				}
			} else {
				// Checks also the form reference.
				if (parent instanceof Form) {
					if (((Form) parent).getFormReference() != null) {
						validateNotNestedTables(((Form) parent).getFormReference(), isTable);
					}
				}
			}
			validateNotNestedTables(parent.getParent(), isTable);
		}
	}

}