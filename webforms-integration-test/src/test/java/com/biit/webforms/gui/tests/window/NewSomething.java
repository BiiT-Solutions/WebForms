package com.biit.webforms.gui.tests.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.biit.webforms.gui.tests.exceptions.OrganizationNotEditableException;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewSomething extends VaadinGuiWindow {

	public static final String NAME_FIELD_CAPTION = "Name";
	public static final String ORGANIZATION_COMBOBOX_CAPTION = "Group";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";

	private TextFieldElement getNewFormNameTextField() {
		return $(TextFieldElement.class).caption(NAME_FIELD_CAPTION).first();
	}

	private ComboBoxElement getOrganizationComboBox() {
		return $(ComboBoxElement.class).caption(ORGANIZATION_COMBOBOX_CAPTION).first();
	}

	private ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}

	public void createNewThing(String formName) {
		getNewFormNameTextField().setValue(formName);
		getAcceptButton().click();
	}

	public void createNewThing(String formName, String editOrganization) throws OrganizationNotEditableException {
		getNewFormNameTextField().setValue(formName);
		editOrganization(editOrganization);
		getAcceptButton().click();
	}

	public void editOrganization(String organizationName) throws OrganizationNotEditableException {
		if (getOrganizationComboBox().isEnabled()) {
			getOrganizationComboBox().selectByText(organizationName);
		} else {
			throw new OrganizationNotEditableException();
		}
	}

}
