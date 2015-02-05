package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.test.exceptions.OrganizationNotEditableException;

@Test(groups = "basicFunctionality")
public class BasicFuncionalityTests extends WebFormsTester {

	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";
	private static final String ORGANIZATION_NAME = "Bii1";
	private static final String STATUS_FINISHED = "Final Design";

	@Test
	public void testInfoWindow() {
		loginRead1();
		getInfoButton().click();
		clickCloseButtonIfExists();
		logOut();
	}

	@Test
	public void editFormOrganizationWithoutPermissions() {
		loginFormAdmin1();
		try {
			getFormManager().createNewFormWithOrganization(NEW_FORM_NAME, ORGANIZATION_NAME);
			// If no exception is launched the test is not valid
			// The user is not allowed to edit the organization
			Assert.fail();
		} catch (OrganizationNotEditableException e) {
			clickCancelButtonIfExists();
		}
		logOut();
	}

	@Test
	public void editFormWithoutPermissions() {
		// For testing the form edition we need a to create a form
		createNewForm(NEW_FORM_NAME);
		// Starting the real test
		loginRead1();
		goToDesigner();
		checkNotificationIsWarning(getNotification());
		Assert.assertFalse(getDesigner().getSaveButton().isEnabled());
		logOut();
		// Test finished
		// We need to delete the form created for the test, so the database is
		// clean for the next tests
		deleteForm();
	}

	@Test
	public void editFormWithPermissions() {
		// For testing the form edition we need a to create a form
		createNewForm(NEW_FORM_NAME);
		// Starting the real test
		loginFormEdit1();
		goToDesigner();
		getDesigner().addNewCategory();
		getDesigner().saveDesign();
		logOut();
		// Test finished
		// We need to delete the form created for the test, so the database is
		// clean for the next tests
		deleteForm();
	}

	@Test
	public void editBlockWithoutPermissions() {
		// For testing the block edition we need a to create a block
		createNewBlock(NEW_BLOCK_NAME);
		// Starting the real test
		loginRead1();
		goToBlockManager();
		getBlockManager().selectBlock(0);
		goToDesigner();
		checkNotificationIsWarning(getNotification());
		Assert.assertFalse(getDesigner().getSaveButton().isEnabled());
		logOut();
		// Test finished
		// We need to delete the block created for the test, so the database is
		// clean for the next tests
		deleteBlock();
	}

	@Test
	public void editBlockWithPermissions() {
		// For testing the block edition we need a to create a block
		createNewBlock(NEW_BLOCK_NAME);
		// Starting the real test
		loginBlockEdit1();
		goToBlockManager();
		getBlockManager().selectBlock(0);
		goToDesigner();
		getDesigner().addNewCategory();
		getDesigner().saveDesign();
		logOut();
		// Test finished
		// We need to delete the form created for the test, so the database is
		// clean for the next tests
		deleteBlock();
	}

	@Test
	public void createFormfinishFormCheckStatus() {
		loginFormEdit1();
		getFormManager().createNewForm(NEW_FORM_NAME);
		goToDesigner();
		getDesigner().finishForm();
		clickAcceptButtonIfExists();
		// goToFormManager();
		Assert.assertEquals(getFormManager().getFormStatusComboBox().getValue(), STATUS_FINISHED);
		logOut();
		// Test finished
		// We need to delete the form created for the test, so the database is
		// clean for the next tests
		deleteForm();
	}

}
