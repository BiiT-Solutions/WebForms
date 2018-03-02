package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Group;
import com.vaadin.data.Validator;

public class ValidatorTablesAllowesOnlyGroupsAsRows implements Validator {
	private static final long serialVersionUID = -5320539195645056658L;

	private TreeObject treeObject;

	public ValidatorTablesAllowesOnlyGroupsAsRows(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Boolean isTable = (Boolean) value;
		if (isTable != null && isTable) {
			for (TreeObject child : treeObject.getChildren()) {
				if (!(child instanceof Group)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_TABLE_ONLY_GROUPS_AS_ROWS.translation());
				}
			}
		}
	}
}