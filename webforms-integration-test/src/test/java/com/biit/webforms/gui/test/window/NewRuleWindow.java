package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.CustomComponentElement;
import com.vaadin.testbench.elements.WindowElement;

public class NewRuleWindow extends VaadinGuiWindow {

	// The system is using an empty string to select the component
	public static final String FROM_BUTTON_CAPTION = "";
	public static final String TO_BUTTON_CAPTION = "To";
	public static final String TYPE_COMBOBOX_CAPTION = "Type";
	public static final String OTHERS_CHECKBOX_CAPTION = "Others";

	private static final String CHECKBOX_RETURN_UNCHECKED = "unchecked";
	private static final String CHECKBOX_RETURN_CHECKED = "checked";
	
	private static final String ACCEPT_BUTTON_CAPTION = "Accept";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";

	public void clickFromButton() {
		$$(WindowElement.class).caption(FROM_BUTTON_CAPTION).$(ButtonElement.class).first().click();
	}

	public void clickRemoveFromButton() {
		$$(WindowElement.class).caption(FROM_BUTTON_CAPTION).$(ButtonElement.class).get(1);
	}

	public void clickToButton() {
		$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(ButtonElement.class).first().click();
	}

	public void clickRemoveToButton() {
		$(CustomComponentElement.class).caption(TO_BUTTON_CAPTION).$(ButtonElement.class).get(1).click();
	}

	public ComboBoxElement getTypeCombobox() {
		return $(ComboBoxElement.class).caption(TYPE_COMBOBOX_CAPTION).first();
	}

	public void setTypeComboboxValue(String value) {
		getTypeCombobox().selectByText(value);
	}

	public void checkOthersCheckbox() {
		if (getOthersCheckBoxValue().equals(CHECKBOX_RETURN_UNCHECKED)) {
			clickOthersCheckBox();
		}
	}

	public void uncheckOthersCheckbox() {
		if (getOthersCheckBoxValue().equals(CHECKBOX_RETURN_CHECKED)) {
			clickOthersCheckBox();
		}
	}

	public String getOthersCheckBoxValue() {
		return $(CheckBoxElement.class).caption(OTHERS_CHECKBOX_CAPTION).first().getValue();
	}

	public void clickOthersCheckBox() {
		$(CheckBoxElement.class).caption(OTHERS_CHECKBOX_CAPTION).first().click();
	}

	public void clickAcceptButton(){
		$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first().click();
	}
	
	public void clickCancelButton(){
		$(ButtonElement.class).caption(CANCEL_BUTTON_CAPTION).first().click();
	}
}
