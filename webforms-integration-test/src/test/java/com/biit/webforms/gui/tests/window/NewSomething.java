package com.biit.webforms.gui.tests.window;

import com.biit.webforms.gui.tests.exceptions.OrganizationNotEditableException;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewSomething extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.components.WindowNameGroup";
	private static final String NAME_FIELD_CAPTION = "Name";
	private static final String ORGANIZATION_COMBOBOX_CAPTION = "Group";
	
	private TextFieldElement getNewFormNameTextField() {
		return getWindow().$(TextFieldElement.class).caption(NAME_FIELD_CAPTION).first();
	}

	private ComboBoxElement getOrganizationComboBox() {
		return getWindow().$(ComboBoxElement.class).caption(ORGANIZATION_COMBOBOX_CAPTION).first();
	}

	public void createNewThing(String formName) {
		getNewFormNameTextField().setValue(formName);
		clickAccept();
	}

	public void createNewThing(String formName, String editOrganization) throws OrganizationNotEditableException {
		getNewFormNameTextField().setValue(formName);
		editOrganization(editOrganization);
		clickAccept();
	}

	public void editOrganization(String organizationName) throws OrganizationNotEditableException {
		if (getOrganizationComboBox().isEnabled()) {
			getOrganizationComboBox().selectByText(organizationName);
		} else {
			throw new OrganizationNotEditableException();
		}
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}
