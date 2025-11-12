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

public class LoginTests extends WebFormsTester {

    private static final String BADUSERNAME = "badUserName@bad.com";
    private static final String BADPASSWORD = "badPassword";
    private static final String NEW_FORM_NAME = "new_form_1";
    private static final String NEW_BLOCK_NAME = "new_block_1";
    private static final String USER_WITHOUT_PERMISION = "paul@test.com";
    private static final String PASSWORD = "my-password";

    @Test(groups = "login")
    public void testLoginFail() {
        login(BADUSERNAME, BADPASSWORD);
        checkNotificationIsError(getNotification());
    }

    @Test(groups = "login")
    public void testLoginLogout() {
        loginRead1();
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

    @Test(groups = "login")
    public void testLoginWithoutRightsToManageForm() {
        loginRead1();
        getFormManagerPage().clickNewButton();
        Assert.assertFalse(getFormManagerPage().getNewFormButton().isEnabled());
        // Close New popover menu -- IMPORTANT !!
        getFormManagerPage().closeNewPopover();
        Assert.assertNull(getFormManagerPage().getRemoveForm());
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
    public void testLoginWithRightsToManageBlockButNotDeleteBlock() {
        loginBlockEdit1();
        goToBlockManagerPage();
        Assert.assertTrue(getBlockManagerPage().getNewBlockButton().isEnabled());
        Assert.assertFalse(getBlockManagerPage().getRemoveBlockButton().isEnabled());
        logOut();
    }

    @Test(groups = "login")
    public void testLoginWithRightsToManageButNotDeleteForm() {
        loginFormEdit1();
        getFormManagerPage().clickNewButton();
        Assert.assertTrue(getFormManagerPage().getNewFormButton().isEnabled());
        // Close New popover menu -- IMPORTANT !!
        getFormManagerPage().closeNewPopover();

        Assert.assertNull(getFormManagerPage().getRemoveForm());
    }

    @Test(groups = "login")
    public void testLoginWithRightsToManageForm() {
        loginFormAdmin1();
        getFormManagerPage().createNewForm(NEW_FORM_NAME);
        getFormManagerPage().deleteForm(1);
        logOut();
    }

    @Test(groups = "login")
    public void testLoginWithoutPermission() {
        login(USER_WITHOUT_PERMISION, PASSWORD);
        checkNotificationIsError(getNotification());
        loginRead1();
        logOut();
    }
}
