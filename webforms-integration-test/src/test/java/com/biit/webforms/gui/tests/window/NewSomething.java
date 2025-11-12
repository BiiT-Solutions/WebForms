package com.biit.webforms.gui.tests.window;

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
