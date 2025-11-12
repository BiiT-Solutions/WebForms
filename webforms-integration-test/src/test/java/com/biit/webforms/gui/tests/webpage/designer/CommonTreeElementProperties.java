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

import com.biit.gui.tester.VaadinGuiView;
import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.vaadin.testbench.elements.AbstractComponentElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextAreaElement;
import com.vaadin.testbench.elements.TextFieldElement;

/**
 * This class defines the common element that can appear in a properties view.<br>
 */
public class CommonTreeElementProperties extends VaadinGuiView {

	public void checkEditable(AbstractComponentElement element) throws FieldNotEditableException {
		if (!(element.isEnabled()) || element.isReadOnly()) {
			throw new FieldNotEditableException();
		}
	}

	public TextFieldElement getTextField(String textFieldCaption) {
		return $(TextFieldElement.class).caption(textFieldCaption).first();
	}

	public TextAreaElement getTextArea(String textAreaCaption) {
		return $(TextAreaElement.class).caption(textAreaCaption).first();
	}

	public ComboBoxElement getComboBox(String comboboxCaption) {
		return $(ComboBoxElement.class).caption(comboboxCaption).first();
	}

	public CheckBoxElement getCheckBox(String checkBoxCaption) {
		return $(CheckBoxElement.class).caption(checkBoxCaption).first();
	}

	public String getTextFieldValue(String textFieldCaption) {
		return getTextField(textFieldCaption).getValue();
	}

	public void setTextFieldValue(String textFieldCaption, String value) throws FieldNotEditableException {
		checkEditable(getTextField(textFieldCaption));
		getTextField(textFieldCaption).setValue(value);
		getTextField(textFieldCaption).click();
	}

	public String getTextAreaValue(String textAreaCaption) {
		return getTextArea(textAreaCaption).getValue();
	}

	public void setTextAreaValue(String textAreaCaption, String value) throws FieldNotEditableException {
		checkEditable(getTextArea(textAreaCaption));
		getTextArea(textAreaCaption).setValue(value);
	}

	public String getComboBoxValue(String comboboxCaption) {
		return getComboBox(comboboxCaption).getValue();
	}

	public void setComboBoxValue(String comboboxCaption, String value) throws FieldNotEditableException {
		checkEditable(getComboBox(comboboxCaption));
		getComboBox(comboboxCaption).selectByText(value);
	}

	public String getCheckBoxValue(String checkBoxCaption) {
		return getCheckBox(checkBoxCaption).getValue();
	}

	public void clickCheckBox(String checkBoxCaption) throws FieldNotEditableException {
		checkEditable(getCheckBox(checkBoxCaption));
		getCheckBox(checkBoxCaption).click();
	}
}
