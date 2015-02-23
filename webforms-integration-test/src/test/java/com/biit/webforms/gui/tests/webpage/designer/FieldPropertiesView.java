package com.biit.webforms.gui.tests.webpage.designer;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

public class FieldPropertiesView extends TechnicalNameProperty {

	private static final String FIELD_TEXT_FIELD_CAPTION = "Field";

	public String getFieldValue() {
		return getTextFieldValue(FIELD_TEXT_FIELD_CAPTION);
	}

	public void setFieldValue(String value) throws FieldNotEditableException {
		setTextFieldValue(FIELD_TEXT_FIELD_CAPTION, value);
	}
}
