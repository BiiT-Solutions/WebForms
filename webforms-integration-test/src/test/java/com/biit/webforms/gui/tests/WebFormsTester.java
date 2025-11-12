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

import com.biit.gui.tester.VaadinGuiTester;
import com.biit.webforms.gui.tests.webpage.*;
import com.vaadin.testbench.elements.ButtonElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

/**
 * Parent of all the windows created for testing the UI.<br>
 * This class has simple functions to get the buttons common to all windows in
 * the application.
 */
public class WebFormsTester extends VaadinGuiTester {

    protected final String WEBFORMS_READ_BIIT1 = "webforms_read@biit1.com";
    protected final String WEBFORMS_FORM_EDIT_BIIT1 = "webforms_form_edit@biit1.com";
    protected final String WEBFORMS_BLOCK_EDIT_BIIT1 = "webforms_block_edit@biit1.com";
    protected final String WEBFORMS_FORM_ADMIN_BIIT1 = "webforms_form_admin@biit1.com";

    protected final String USER_PASSWORD = "my-password";

    private static final String SCREENSHOT_TYPE = ".png";

    private final Login loginPage;
    private final FormManager formManager;
    private final BlockManager blockManager;
    private final Designer designer;
    private final FlowManager flowManager;
    private final Webservice webservice;
    private final Validation validation;

    private static final String FORMS_BUTTON_CAPTION = "Forms";
    private static final String BLOCKS_BUTTON_CAPTION = "Blocks";
    private static final String SETTINGS_BUTTON_CAPTION = "Settings";
    private static final String INFO_BUTTON_CAPTION = "Info";
    private static final String LOGOUT_BUTTON_CAPTION = "Log Out";
    private static final String ACCEPT_BUTTON_CAPTION = "Accept";
    private static final String CANCEL_BUTTON_CAPTION = "Cancel";
    private static final String CLOSE_BUTTON_CAPTION = "Close";

    private static final String DESIGN_BUTTON_CAPTION = "Design";
    private static final String FLOW_BUTTON_CAPTION = "Flow";
    private static final String VALIDATION_BUTTON_CAPTION = "Validation";
    private static final String WEBSERVICE_BUTTON_CAPTION = "Webservice Calls";

    public WebFormsTester() {
        super();
        loginPage = new Login();
        addWebpage(loginPage);
        formManager = new FormManager();
        addWebpage(formManager);
        blockManager = new BlockManager();
        addWebpage(blockManager);
        designer = new Designer();
        addWebpage(designer);
        flowManager = new FlowManager();
        addWebpage(flowManager);
        webservice = new Webservice();
        addWebpage(webservice);
        validation = new Validation();
        addWebpage(validation);
    }

    public void clickAcceptButtonIfExists() {
        if (getAcceptButton() != null) {
            getAcceptButton().click();
        }
    }

    public void clickCancelButtonIfExists() {
        if (getCancelButton() != null) {
            getCancelButton().click();
        }
    }

    public void clickCloseButtonIfExists() {
        if (getCloseButton() != null) {
            getCloseButton().click();
        }
    }

    protected void createNewBlock(String blockName) {
        loginFormAdmin1();
        goToBlockManagerPage();
        getBlockManagerPage().createNewBlock(blockName);
    }

    protected void createNewBlockAndLogout(String blockName) {
        loginFormAdmin1();
        goToBlockManagerPage();
        getBlockManagerPage().createNewBlock(blockName);
        logOut();
    }

    protected void createNewEmptyForm(String formName) {
        loginFormAdmin1();
        getFormManagerPage().createNewForm(formName);
    }

    public void createNewFormAndLogout(String formName) {
        loginFormAdmin1();
        getFormManagerPage().createNewForm(formName);
        logOut();
    }

    protected void createNewFormWithAdminRights(String formName) {
        loginFormAdmin1();
        getFormManagerPage().createNewForm(formName);
    }

    protected void deleteBlock() {
        loginFormAdmin1();
        goToBlockManagerPage();
        getBlockManagerPage().deleteBlock();
        logOut();
    }

    /**
     * Logs in as admin and deletes the first form existent
     */
    protected void deleteForm() {
        loginFormAdmin1();
        getFormManagerPage().deleteForm(1);
        logOut();
    }

    public ButtonElement getAcceptButton() {
        if ($(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).exists()) {
            return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
        } else {
            return null;
        }
    }

    public BlockManager getBlockManagerPage() {
        return blockManager;
    }

    public ButtonElement getBlocksButton() {
        return $(ButtonElement.class).caption(BLOCKS_BUTTON_CAPTION).first();
    }

    public ButtonElement getCancelButton() {
        if ($(ButtonElement.class).caption(CANCEL_BUTTON_CAPTION).exists()) {
            return $(ButtonElement.class).caption(CANCEL_BUTTON_CAPTION).first();
        } else {
            return null;
        }
    }

    public ButtonElement getCloseButton() {
        if ($(ButtonElement.class).caption(CLOSE_BUTTON_CAPTION).exists()) {
            return $(ButtonElement.class).caption(CLOSE_BUTTON_CAPTION).first();
        } else {
            return null;
        }
    }

    public ButtonElement getDesignButton() {
        return $(ButtonElement.class).caption(DESIGN_BUTTON_CAPTION).first();
    }

    public Designer getDesignerPage() {
        return designer;
    }

    public Webservice getWebservicePage() {
        return webservice;
    }

    public ButtonElement getFlowButton() {
        return $(ButtonElement.class).caption(FLOW_BUTTON_CAPTION).first();
    }

    public ButtonElement getWebserviceButton() {
        return $(ButtonElement.class).caption(WEBSERVICE_BUTTON_CAPTION).first();
    }

    public FlowManager getFlowManagerPage() {
        return flowManager;
    }

    public FormManager getFormManagerPage() {
        return formManager;
    }

    public ButtonElement getFormsButton() {
        return $(ButtonElement.class).caption(FORMS_BUTTON_CAPTION).first();
    }

    public ButtonElement getInfoButton() {
        showSettingsMenu();
        return $(ButtonElement.class).caption(INFO_BUTTON_CAPTION).first();
    }

    public Login getLoginPage() {
        return loginPage;
    }

    public ButtonElement getLogoutButton() {
        showSettingsMenu();
        return $(ButtonElement.class).caption(LOGOUT_BUTTON_CAPTION).first();
    }

    public ButtonElement getSettingsButton() {
        return $(ButtonElement.class).caption(SETTINGS_BUTTON_CAPTION).first();
    }

    public ButtonElement getValidationButton() {
        return $(ButtonElement.class).caption(VALIDATION_BUTTON_CAPTION).first();
    }

    public Validation getValidationPage() {
        return validation;
    }

    public void goToBlockManagerPage() {
        getBlocksButton().click();
    }

    public void goToDesignerPage() {
        getDesignButton().click();
    }

    public void goToFlowManagerPage() {
        getFlowButton().click();
    }

    public void goToWebservicePage() {
        getWebserviceButton().click();
    }

    public void goToFormManagerPage() {
        getFormsButton().click();
        clickAcceptButtonIfExists();
        if (getFormsButton() != null) {
            getFormsButton().waitForVaadin();
        }
    }

    public void goToValidationPage() {
        getValidationButton().click();
        getValidationButton().waitForVaadin();
    }

    public void login(String username, String password) {
        mainPage();
        loginPage.login(username, password);
    }

    public void loginBlockEdit1() {
        login(WEBFORMS_BLOCK_EDIT_BIIT1, USER_PASSWORD);
    }

    public void loginFormAdmin1() {
        login(WEBFORMS_FORM_ADMIN_BIIT1, USER_PASSWORD);
    }

    public void loginFormEdit1() {
        login(WEBFORMS_FORM_EDIT_BIIT1, USER_PASSWORD);
    }

    public void loginRead1() {
        login(WEBFORMS_READ_BIIT1, USER_PASSWORD);
    }

    public void logOut() {
        sleep(500);
        getLogoutButton().click();
        clickAcceptButtonIfExists();
        sleep(1000);
    }

    public void showSettingsMenu() {
        getSettingsButton().click();
    }

    /**
     * Takes a screenshot of the webpage and stores it in the Temp folder.<br>
     * Only works when using the phantomJS driver.
     *
     * @param screenshotName
     */
    protected void takeScreenshot(String screenshotName) {
        if (isHeadlessTesting()) {
            File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File(System.getProperty("java.io.tmpdir") + File.separator
                        + screenshotName + SCREENSHOT_TYPE), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
