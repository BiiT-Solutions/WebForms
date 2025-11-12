package com.biit.webforms.gui.tests.webpage.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Test)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;

public class QuestionPropertiesView extends CommonCategoryGroupQuestionProperties {

	private static final String DESCRIPTION_TEXT_AREA_CAPTION = "Description";
	private static final String ABBREVIATION_TEXT_FIELD_CAPTION = "Abbreviature";
	private static final String ALIAS_TEXT_FIELD_CAPTION = "Alias";
	private static final String ANSWER_TYPE_COMBOBOX_CAPTION = "Answer type";
	private static final String ANSWER_FORMAT_COMBOBOX_CAPTION = "Answer format";
	private static final String ANSWER_SUBFORMAT_COMBOBOX_CAPTION = "Answer subformat";
	private static final String ANSWER_DEFAULT_VALUE_COMBOBOX_CAPTION = "Default value";
	private static final String HORIZONTAL_CHECKBOX_CAPTION = "Horizontal";
	private static final String MANDATORY_CHECKBOX_CAPTION = "Mandatory";

	public String getDescription() {
		return getTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION);
	}
	
	public void setDescription(String value) throws FieldNotEditableException {
		setTextAreaValue(DESCRIPTION_TEXT_AREA_CAPTION, value);
	}
	
	public String getAlias() {
		return getTextFieldValue(ALIAS_TEXT_FIELD_CAPTION);
	}
	
	public void setAlias(String value) throws FieldNotEditableException {
		setTextFieldValue(ALIAS_TEXT_FIELD_CAPTION, value);
	}
	
	public String getAbbreviation() {
		return getTextFieldValue(ABBREVIATION_TEXT_FIELD_CAPTION);
	}

	public void setAbbreviation(String value) throws FieldNotEditableException {
		setTextFieldValue(ABBREVIATION_TEXT_FIELD_CAPTION, value);
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
	
	public String getAnswerDefaultValueComboBoxValue() {
		return getComboBoxValue(ANSWER_DEFAULT_VALUE_COMBOBOX_CAPTION);
	}
	
	public void setAnswerDefaultValueComboBoxValue(String value) throws FieldNotEditableException {
		setComboBoxValue(ANSWER_DEFAULT_VALUE_COMBOBOX_CAPTION, value);
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
	
	public boolean ismandatoryEnabled(){
		return getCheckBox(MANDATORY_CHECKBOX_CAPTION).isEnabled();
	}
	
	public boolean isHorizontalEnabled(){
		return getCheckBox(HORIZONTAL_CHECKBOX_CAPTION).isEnabled();
	}
}
