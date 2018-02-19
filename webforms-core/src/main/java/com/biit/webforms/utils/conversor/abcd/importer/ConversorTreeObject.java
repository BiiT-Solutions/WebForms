package com.biit.webforms.utils.conversor.abcd.importer;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Abstract conversor of tree objects. All elements of forms in abcd and
 * webforms implement a treeObject.
 *
 * @param <O>
 * @param <D>
 */
public abstract class ConversorTreeObject<O extends TreeObject, D extends TreeObject> extends ConversorClass<O, D> {

	@Override
	public void copyData(O origin, D destiny) {
		try {
			destiny.setName(origin.getName());
			if (origin.getLabel() != null && !origin.getLabel().isEmpty()) {
				destiny.setLabel(origin.getLabel());
			} else {
				destiny.setLabel(origin.getName());
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}
}
