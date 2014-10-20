package com.biit.webforms.utils.conversor;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;

public abstract class ConversorTreeObject<O extends TreeObject,D extends TreeObject> extends ConversorClass<O,D>{

	@Override
	public void copyData(O origin, D destiny) {
		try {
			destiny.setName(origin.getName());
			destiny.setLabel(origin.getLabel());
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

}
