package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class TechnicalNameProperty extends CommonTreeElementProperties {

	private static final String TECHNICAL_NAME_FIELD_CAPTION = "Technical Name";

	public String getTechnicalName() {
		return getTextFieldValue(TECHNICAL_NAME_FIELD_CAPTION);
	}

	public void setTechnicalName(String value) throws FieldNotEditableException {
		setTextFieldValue(TECHNICAL_NAME_FIELD_CAPTION, value);
	}
}
