package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class FormPropertiesView extends CommonTreeElementProperties {

	private static final String NAME_FIELD_CAPTION = "Name";
	private static final String VERSION_FIELD_CAPTION = "Version";
	private static final String DESCRIPTION_TEXT_AREA_CAPTION = "Description";

	public String getName() {
		return getTextFieldValue(NAME_FIELD_CAPTION);
	}

	public void setName(String value) throws FieldNotEditableException {
		setTextFieldValue(NAME_FIELD_CAPTION, value);
	}

	public String getVersionValue() {
		return getTextFieldValue(VERSION_FIELD_CAPTION);
	}

	public void setVersionValue(String value) throws FieldNotEditableException {
		setTextFieldValue(VERSION_FIELD_CAPTION, value);
	}

	public String getDescription() {
		return getTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION);
	}

	public void setDescription(String value) throws FieldNotEditableException {
		setTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION, value);
	}
}
