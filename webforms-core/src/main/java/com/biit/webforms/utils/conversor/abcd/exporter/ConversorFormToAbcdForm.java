package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.Form;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorTreeObject;

/**
 * Conversor from Abcd Form to Webforms form.
 * 
 * Both forms only have categories as childs.
 *
 */
public class ConversorFormToAbcdForm extends ConversorTreeObject<com.biit.webforms.persistence.entity.Form, Form> {

	private ConversorCategoryToAbcdCategory conversorCategory = new ConversorCategoryToAbcdCategory();

	@Override
	public Form createDestinyInstance() {
		return new Form();
	}

	@Override
	public void copyData(com.biit.webforms.persistence.entity.Form origin, Form destiny) {
		// Copy base data
		try {
			destiny.setLabel(origin.getLabel());
		} catch (FieldTooLongException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		// Create copy of the childs and assign.
		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof com.biit.webforms.persistence.entity.Category)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			TreeObject convertedChild = conversorCategory.convert((com.biit.webforms.persistence.entity.Category) child);
			try {
				destiny.addChild(convertedChild);
			} catch (NotValidChildException | ElementIsReadOnly e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}

		}
	}
}
