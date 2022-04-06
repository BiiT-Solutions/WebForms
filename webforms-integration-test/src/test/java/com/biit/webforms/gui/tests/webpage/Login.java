package com.biit.webforms.gui.tests.webpage;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import org.openqa.selenium.NoSuchElementException;

public class Login extends VaadinGuiWebpage {

    private final static String LOGIN_URL = "http://localhost:9081";
    private final static String USER_NAME_LOGIN_FORM = "userNameLoginForm";
    private final static String USER_PASS_LOGIN_FORM = "userPassLoginForm";
    private final static String USER_PASS_LOGIN_BUTTON_ID = "loginButton";

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
