package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class TechnicalNameProperty extends CommonTreeElementProperties {

	private static final String TECHNICAL_NAME_FIELD_CAPTION = "Technical Name";

	public String getTechnicalNameFieldValue() {
		return getTextFieldValue(TECHNICAL_NAME_FIELD_CAPTION);
	}

	public void setTechnicalNameFieldValue(String value) throws FieldNotEditableException {
		setTextFieldValue(TECHNICAL_NAME_FIELD_CAPTION, value);
	}
}
