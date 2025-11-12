package com.biit.webforms.gui.tests.webpage;

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

import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import org.openqa.selenium.NoSuchElementException;

public class Login extends VaadinGuiWebpage {

    private static final String LOGIN_URL = "http://localhost:9081";
    private static final String USER_NAME_LOGIN_FORM = "userNameLoginForm";
    private static final String USER_PASS_LOGIN_FORM = "userPassLoginForm";
    private static final String USER_PASS_LOGIN_BUTTON_ID = "loginButton";

    private TextFieldElement getUserNameLoginForm() {
        $(TextFieldElement.class).id(USER_NAME_LOGIN_FORM).waitForVaadin();
        return $(TextFieldElement.class).id(USER_NAME_LOGIN_FORM);
    }

    private PasswordFieldElement getUserPassLoginForm() {
        $(PasswordFieldElement.class).id(USER_PASS_LOGIN_FORM).waitForVaadin();
        return $(PasswordFieldElement.class).id(USER_PASS_LOGIN_FORM);
    }

    private ButtonElement getLoginButton() {
        $(ButtonElement.class).id(USER_PASS_LOGIN_BUTTON_ID).waitForVaadin();
        return $(ButtonElement.class).id(USER_PASS_LOGIN_BUTTON_ID);
    }

    public void login(String userName, String password) {
        login(userName, password, 0);
    }

    private void login(String userName, String password, int attempt) {
        try {
            getUserNameLoginForm().waitForVaadin();
            getUserNameLoginForm().setValue(userName);
            getUserNameLoginForm().waitForVaadin();
            getUserPassLoginForm().setValue(password);
            getUserPassLoginForm().waitForVaadin();
            getLoginButton().waitForVaadin();
            getLoginButton().focus();
            getLoginButton().click();
            try {
                getLoginButton().waitForVaadin();
            } catch (Exception e) {
                // Do nothing this is only to make it wait until vaadin responds
                // again. If button is not found it's that vaadin has already
                // changed window
            }
        } catch (NoSuchElementException nse) {
            //From 26-05-21 some login test are still on the previous logout screen and fails.
            if (attempt < 20) {
                sleep(1000);
                login(userName, password, attempt + 1);
            }
        }
    }

    @Override
    public String getWebpageUrl() {
        return LOGIN_URL;
    }
}
