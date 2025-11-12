package com.biit.webforms.gui.tests;

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


import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.tests.exceptions.OrganizationNotEditableException;
import com.vaadin.testbench.elements.TreeTableElement;

import java.time.LocalDateTime;

public class BasicFuncionalityTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final String ORGANIZATION_NAME = "Bii1";
	private static final String STATUS_FINISHED = "Final Design";
	
	@Test(groups = "basicFunctionality")
	public void createFormFinishFormCheckStatus() {
		loginFormEdit1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().finishForm();
		clickAcceptButtonIfExists();
		// goToFormManager();
		Assert.assertEquals(getFormManagerPage().getFormStatusComboBox().getValue(), STATUS_FINISHED);
		logOut();
		deleteForm();
	}
	
	@Test(groups = "basicFunctionality")
	public void editBlockWithoutPermissions() {
		// For testing the block edition we need a to create a block
		createNewBlockAndLogout(NEW_BLOCK_NAME);
		// Starting the real test
		loginRead1();
		goToBlockManagerPage();
		goToDesignerPage();
		checkNotificationIsWarning(getNotification());
		Assert.assertFalse(getDesignerPage().getSaveButton().isEnabled());
		logOut();
		deleteBlock();
	}

	@Test(groups = "basicFunctionality")
	public void editBlockWithPermissions() {
		// For testing the block edition we need a to create a block
		createNewBlockAndLogout(NEW_BLOCK_NAME);
		// Starting the real test
		loginBlockEdit1();
		goToBlockManagerPage();
		goToDesignerPage();
		getDesignerPage().addNewCategory();
		getDesignerPage().saveDesign();
		logOut();
		deleteBlock();
	}

	@Test(groups = "basicFunctionality")
	public void editFormOrganizationWithoutPermissions() {
		loginFormAdmin1();
		try {
			getFormManagerPage().createNewFormWithOrganization(NEW_FORM_NAME, ORGANIZATION_NAME);
			// If no exception is launched the test is not valid
			// The user is not allowed to edit the organization
			Assert.fail();
		} catch (OrganizationNotEditableException e) {
			clickCancelButtonIfExists();
		}
		logOut();
	}

	@Test(groups = "basicFunctionality")
	public void editFormWithoutPermissions() {
		// For testing the form edition we need a to create a form
		createNewFormAndLogout(NEW_FORM_NAME);
		// Starting the real test
		loginRead1();
		goToDesignerPage();
		checkNotificationIsWarning(getNotification());
		Assert.assertFalse(getDesignerPage().getSaveButton().isEnabled());
		logOut();
		deleteForm();
	}

	@Test(groups = "basicFunctionality")
	public void editFormWithPermissions() {
		// For testing the form edition we need a to create a form
		createNewFormAndLogout(NEW_FORM_NAME);
		// Starting the real test
		loginFormEdit1();
		goToDesignerPage();
		getDesignerPage().addNewCategory();
		getDesignerPage().saveDesign();
		logOut();
		deleteForm();
	}

	@Test(groups = "basicFunctionality")
	public void testInfoWindow() {
		loginRead1();
		getInfoButton().click();
		clickCloseButtonIfExists();
		logOut();
	}
	
	@Test(groups = "basicFunctionality")
	public void createFormWithHiddenElements() throws FieldNotEditableException {
		loginFormAdmin1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().addNewCategory();
		getDesignerPage().getHideButton().click();
		getDesignerPage().addNewInputQuestion();
		getDesignerPage().saveDesign();
		//clickDeleteButton
		logOut();
		deleteForm();
	}
	
	@Test(groups = "basicFunctionality", expectedExceptions = NullPointerException.class)
	public void deleteHiddenElementFromForm() throws FieldNotEditableException {
		loginFormAdmin1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().addNewCategory();
		getDesignerPage().getHideButton().click();
		getDesignerPage().addNewInputQuestion();
		getDesignerPage().getHideButton().click();
		getDesignerPage().saveDesign();
		getDesignerPage().clickDeleteButton();
		getDesignerPage().addNewInputQuestion();
		getDesignerPage().saveDesign();
		try {
			getFormTable().getCell(1, 0).click();
			getDesignerPage().getHideButton().click();
		} finally {
			logOut();
			deleteForm();
		}
	}
	
	@Test(groups = "basicFunctionality", expectedExceptions = NullPointerException.class)
	public void checkHiddenButtonDisabled() throws FieldNotEditableException {
		loginFormAdmin1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		goToDesignerPage();
		getDesignerPage().addNewCategory();
		takeScreenshot(LocalDateTime.now() + "_before_hidding_button");
		getDesignerPage().getHideButton().click();
		takeScreenshot(LocalDateTime.now() + "_after_hidding_button");
		getDesignerPage().addNewInputQuestion();
		getDesignerPage().saveDesign();
		logOut();
		loginFormAdmin1();
		goToDesignerPage();
		try {
			getFormTable().getCell(1, 0).click();
			takeScreenshot(LocalDateTime.now() + "_loaded_hidding_button");
			getDesignerPage().getHideButton().click();
		} finally {
			logOut();
			deleteForm();
		}
	}
	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}
}
