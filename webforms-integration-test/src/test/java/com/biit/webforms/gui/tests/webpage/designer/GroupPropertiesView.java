package com.biit.webforms.gui.tests.webpage.designer;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

public class GroupPropertiesView extends CommonCategoryGroupQuestionProperties {

	private static final String REPEATABLE_CHECKBOX_CAPTION = "Repeatable";

	public String getRepeatableCheckBoxValue() {
		return getCheckBoxValue(REPEATABLE_CHECKBOX_CAPTION);
	}

	public void clickRepeatableCheckBox() throws FieldNotEditableException {
		clickCheckBox(REPEATABLE_CHECKBOX_CAPTION);
	}
}
