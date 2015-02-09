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
		getFormManager().createNewForm(NEW_FORM_NAME);
		getFormManager().deleteForm(1);
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageButNotDeleteForm() {
		loginFormEdit1();
		getFormManager().clickNewButton();
		Assert.assertTrue(getFormManager().getNewFormButton().isEnabled());
		// Close New popover menu -- IMPORTANT !!
		getFormManager().closeNewPopover();
		try {
			getFormManager().getRemoveForm();
			Assert.fail();
		} catch (NoSuchElementException e) {
			// Nothing to do
		}
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithoutRightsToManageForm() {
		loginRead1();
		getFormManager().clickNewButton();
		Assert.assertFalse(getFormManager().getNewFormButton().isEnabled());
		// Close New popover menu -- IMPORTANT !!
		getFormManager().closeNewPopover();
		try {
			getFormManager().getRemoveForm();
			Assert.fail();
		} catch (NoSuchElementException e) {
			// Nothing to do
		}
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageBlockButNotDeleteBlock() {
		loginBlockEdit1();
		goToBlockManager();
		Assert.assertTrue(getBlockManager().getNewBlockButton().isEnabled());
		Assert.assertFalse(getBlockManager().getRemoveBlockButton().isEnabled());
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithRightsToManageBlock() {
		loginFormAdmin1();
		goToBlockManager();
		getBlockManager().createNewBlock(NEW_BLOCK_NAME);
		getBlockManager().deleteBlock(0);
		logOut();
	}

	@Test(groups = "login")
	public void testLoginWithoutRightsToManageBlock() {
		loginRead1();
		goToBlockManager();
		Assert.assertFalse(getBlockManager().getNewBlockButton().isEnabled());
		Assert.assertFalse(getBlockManager().getRemoveBlockButton().isEnabled());
		logOut();
	}
}
