package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class CommonCategoryGroupQuestionProperties extends TechnicalNameProperty {

	private static final String LABEL_TEXT_AREA_CAPTION = "Label";

	public String getLabel() {
		return getTextAreaValue(LABEL_TEXT_AREA_CAPTION);
	}

	public void setLabel(String value) throws FieldNotEditableException {
		setTextAreaValue(LABEL_TEXT_AREA_CAPTION, value);
	}
}
