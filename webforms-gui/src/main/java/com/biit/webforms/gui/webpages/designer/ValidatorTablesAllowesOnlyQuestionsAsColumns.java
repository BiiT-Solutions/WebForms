package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.vaadin.data.Validator;

public class ValidatorTablesAllowesOnlyQuestionsAsColumns implements Validator {
	private static final long serialVersionUID = -5320539195645056658L;

	private TreeObject treeObject;

	public ValidatorTablesAllowesOnlyQuestionsAsColumns(TreeObject treeObject) {
		this.treeObject = treeObject;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		Boolean isTable = (Boolean) value;
		if (isTable != null && isTable) {
			for (TreeObject group : treeObject.getChildren()) {
				if ((group instanceof Group)) {
					for (TreeObject question : group.getChildren()) {
						if (!(question instanceof Question)) {
							throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_TABLE_ONLY_QUESTIONS_AS_COLUMNS.translation());
						}
					}
				}
			}
		}
	}
}