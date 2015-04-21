package com.biit.webforms.utils.conversor;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;

public class ConversorAbcdFormToForm extends ConversorTreeObject<Form, com.biit.webforms.persistence.entity.Form> {

	private ConversorAbcdCategoryToCategory conversorCategory = new ConversorAbcdCategoryToCategory();

	@Override
	public com.biit.webforms.persistence.entity.Form createDestinyInstance() {
		return new com.biit.webforms.persistence.entity.Form();
	}

	@Override
	public void copyData(Form origin, com.biit.webforms.persistence.entity.Form destiny) {
		// Copy base data
		try {
			destiny.setLabel(origin.getLabel());
		} catch (FieldTooLongException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		// Create copy of the childs and assign.
		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof Category)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			com.biit.webforms.persistence.entity.Category convertedChild = conversorCategory.convert((Category) child);
			try {
				destiny.addChild(convertedChild);
			} catch (NotValidChildException | ElementIsReadOnly e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}

		}
	}
}
