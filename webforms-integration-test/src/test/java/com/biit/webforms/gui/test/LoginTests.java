package com.biit.webforms.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "login")
public class LoginTests extends WebFormsTester {

	private static final String BADUSERNAME = "badUserName@bad.com";
	private static final String BADPASSWORD = "badPassword";
	private static final String NEW_FORM_NAME = "new_form_1";
	private static final String NEW_BLOCK_NAME = "new_block_1";

	@Test
	public void testLoginLogout() {
		loginRead1();
		logOut();
	}

	@Test
	public void testLoginFail() {
		login(BADUSERNAME, BADPASSWORD);
		checkNotificationIsError(getNotification());
	}

	@Test
	public void testLoginWithRightsToManageForm() {
		loginFormAdmin1();
		getFormManager().createNewForm(NEW_FORM_NAME);
		getFormManager().deleteForm(1);
		logOut();
	}

	@Test
	public void testLoginWithRightsToManageButNotDeleteForm() {
		loginFormEdit1();
		Assert.assertTrue(getFormManager().getNewFormButton().isEnabled());
		// Close New menu
		getFormManager().getNewButton().click();
		Assert.assertNull(getFormManager().getRemoveForm());
		logOut();
	}

	@Test
	public void testLoginWithoutRightsToManageForm() {
		loginRead1();
		Assert.assertFalse(getFormManager().getNewFormButton().isEnabled());
		// Close New menu
		getFormManager().getNewButton().click();
		Assert.assertNull(getFormManager().getRemoveForm());
		logOut();
	}

	@Test
	public void testLoginWithRightsToManageBlockButNotDeleteBlock() {
		loginBlockEdit1();
		goToBlockManager();
		Assert.assertTrue(getBlockManager().getNewBlockButton().isEnabled());
		Assert.assertFalse(getBlockManager().getRemoveBlockButton().isEnabled());
		logOut();
	}

	@Test
	public void testLoginWithRightsToManageBlock() {
		loginFormAdmin1();
		goToBlockManager();
		getBlockManager().createNewBlock(NEW_BLOCK_NAME);
		getBlockManager().deleteBlock(0);
		logOut();
	}
	
	@Test
	public void testLoginWithoutRightsToManageBlock() {
		loginRead1();
		goToBlockManager();
		Assert.assertFalse(getBlockManager().getNewBlockButton().isEnabled());
		Assert.assertFalse(getBlockManager().getRemoveBlockButton().isEnabled());
		logOut();
	}
}
