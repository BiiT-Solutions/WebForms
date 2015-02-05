package com.biit.webforms.gui.test.webpage.designer;

import com.biit.webforms.gui.test.exceptions.FieldNotEditableException;

public class QuestionPropertiesView extends CommonCategoryGroupQuestionProperties {

	private static final String DESCRIPTION_TEXT_AREA_CAPTION = "Description";
	private static final String ANSWER_TYPE_COMBOBOX_CAPTION = "Answer type";
	private static final String ANSWER_FORMAT_COMBOBOX_CAPTION = "Answer format";
	private static final String ANSWER_SUBFORMAT_COMBOBOX_CAPTION = "Answer subformat";
	private static final String HORIZONTAL_CHECKBOX_CAPTION = "Horizontal";
	private static final String MANDATORY_CHECKBOX_CAPTION = "Mandatory";

	public String getDescriptionTextAreaValue() {
		return getTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION);
	}

	public void setDescriptionTextAreaValue(String value) throws FieldNotEditableException {
		setTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION, value);
	}

	public String getAnswerTypeComboBoxValue() {
		return getComboBoxValue(ANSWER_TYPE_COMBOBOX_CAPTION);
	}

	public void setAnswerTypeComboBoxValue(String value) throws FieldNotEditableException {
		setComboBoxValue(ANSWER_TYPE_COMBOBOX_CAPTION, value);
	}

	public String getAnswerFormatComboBoxValue() {
		return getComboBoxValue(ANSWER_FORMAT_COMBOBOX_CAPTION);
	}

	public void setAnswerFormatComboBoxValue(String value) throws FieldNotEditableException {
		setComboBoxValue(ANSWER_FORMAT_COMBOBOX_CAPTION, value);
	}

	public String getAnswerSubformatComboBoxValue() {
		return getComboBoxValue(ANSWER_SUBFORMAT_COMBOBOX_CAPTION);
	}

	public void setAnswerSubformatComboBoxValue(String value) throws FieldNotEditableException {
		setComboBoxValue(ANSWER_SUBFORMAT_COMBOBOX_CAPTION, value);
	}

	public String getHorizontalCheckBoxValue() {
		return getCheckBoxValue(HORIZONTAL_CHECKBOX_CAPTION);
	}

	public void clickHorizontalCheckBox() throws FieldNotEditableException {
		clickCheckBox(HORIZONTAL_CHECKBOX_CAPTION);
	}

	public String getMandatoryCheckBoxValue() {
		return getCheckBoxValue(MANDATORY_CHECKBOX_CAPTION);
	}

	public void clickMandatoryCheckBox() throws FieldNotEditableException {
		clickCheckBox(MANDATORY_CHECKBOX_CAPTION);
	}
}
