package com.biit.webforms.gui.test;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends WebFormsTester {

	private static final String BADUSERNAME = "badUserName@bad.com";
	private static final String BADPASSWORD = "badPassword";
	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";

	@Test(groups = "login")
	public void testLoginLogout() {
		loginRead1();
		logOut();
	}

	@Test(groups = "login")
	public void testLoginFail() {
		login(BADUSERNAME, BADPASSWORD);
		checkNotificationIsError(getNotification());
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageForm() {
		loginFormAdmin1();
		getFormManagerPage().createNewForm(NEW_FORM_NAME);
		getFormManagerPage().deleteForm(1);
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageButNotDeleteForm() {
		loginFormEdit1();
		getFormManagerPage().clickNewButton();
		Assert.assertTrue(getFormManagerPage().getNewFormButton().isEnabled());
		// Close New popover menu -- IMPORTANT !!
		getFormManagerPage().closeNewPopover();
		try {
			getFormManagerPage().getRemoveForm();
			Assert.fail();
		} catch (NoSuchElementException e) {
			// Nothing to do
		}
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithoutRightsToManageForm() {
		loginRead1();
		getFormManagerPage().clickNewButton();
		Assert.assertFalse(getFormManagerPage().getNewFormButton().isEnabled());
		// Close New popover menu -- IMPORTANT !!
		getFormManagerPage().closeNewPopover();
		try {
			getFormManagerPage().getRemoveForm();
			Assert.fail();
		} catch (NoSuchElementException e) {
			// Nothing to do
		}
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageBlockButNotDeleteBlock() {
		loginBlockEdit1();
		goToBlockManagerPage();
		Assert.assertTrue(getBlockManagerPage().getNewBlockButton().isEnabled());
		Assert.assertFalse(getBlockManagerPage().getRemoveBlockButton().isEnabled());
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageBlock() {
		loginFormAdmin1();
		goToBlockManagerPage();
		getBlockManagerPage().createNewBlock(NEW_BLOCK_NAME);
		getBlockManagerPage().deleteBlock();
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithoutRightsToManageBlock() {
		loginRead1();
		goToBlockManagerPage();
		Assert.assertFalse(getBlockManagerPage().getNewBlockButton().isEnabled());
		Assert.assertFalse(getBlockManagerPage().getRemoveBlockButton().isEnabled());
		logOut();
	}
}
