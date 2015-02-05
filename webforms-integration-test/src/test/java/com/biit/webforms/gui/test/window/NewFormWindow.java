package com.biit.webforms.gui.test.window;

import com.biit.webforms.gui.test.exceptions.OrganizationNotEditableException;

public class NewFormWindow extends NewSomething {

	public void createNewForm(String formName) {
		createNewThing(formName);
	}
	
	public void createNewFormWithOrganization(String formName, String organizationName)
			throws OrganizationNotEditableException {
		createNewThing(formName, organizationName);
	}
}
