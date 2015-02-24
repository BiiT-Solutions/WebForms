package com.biit.webforms.gui.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.OrganizationNotEditableException;

public class BasicFuncionalityTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final String ORGANIZATION_NAME = "Bii1";
	private static final String STATUS_FINISHED = "Final Design";
	
	@Test(groups = "basicFunctionality")
	public void createFormfinishFormCheckStatus() {
		printTestNameInDebugTrace("createFormfinishFormCheckStatus");
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
		printTestNameInDebugTrace("editBlockWithoutPermissions");
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
		printTestNameInDebugTrace("editBlockWithPermissions");
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
		printTestNameInDebugTrace("editFormOrganizationWithoutPermissions");
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
		printTestNameInDebugTrace("editFormWithoutPermissions");
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
		printTestNameInDebugTrace("editFormWithPermissions");
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
		printTestNameInDebugTrace("testInfoWindow");
		loginRead1();
		getInfoButton().click();
		clickCloseButtonIfExists();
		logOut();
	}
}
