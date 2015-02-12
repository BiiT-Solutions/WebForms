package com.biit.webforms.gui.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.biit.gui.tester.VaadinGuiTester;
import com.biit.webforms.gui.test.webpage.BlockManager;
import com.biit.webforms.gui.test.webpage.Designer;
import com.biit.webforms.gui.test.webpage.FlowManager;
import com.biit.webforms.gui.test.webpage.FormManager;
import com.biit.webforms.gui.test.webpage.Login;
import com.biit.webforms.gui.test.webpage.Validation;
import com.vaadin.testbench.elements.ButtonElement;

/**
 * Parent of all the windows created for testing the UI.<br>
 * This class has simple functions to get the buttons common to all windows in
 * the application.
 *
 */
public class WebFormsTester extends VaadinGuiTester {

	protected final String WEBFORMS_READ_BIIT1 = "webforms_read@biit1.com";
	protected final String WEBFORMS_FORM_EDIT_BIIT1 = "webforms_form_edit@biit1.com";
	protected final String WEBFORMS_BLOCK_EDIT_BIIT1 = "webforms_block_edit@biit1.com";
	protected final String WEBFORMS_FORM_ADMIN_BIIT1 = "webforms_form_admin@biit1.com";
	protected final String WEBFORMS_APP_ADMIN_BIIT1 = "webforms_app_admin@biit1.com";

	protected final String WEBFORMS_READ_BIIT2 = "webforms_read@biit2.com";
	protected final String WEBFORMS_FORM_EDIT_BIIT2 = "webforms_form_edit@biit2.com";
	protected final String WEBFORMS_BLOCK_EDIT_BIIT2 = "webforms_block_edit@biit2.com";
	protected final String WEBFORMS_FORM_ADMIN_BIIT2 = "webforms_form_admin@biit2.com";
	protected final String WEBFORMS_APP_ADMIN_BIIT2 = "webforms_app_admin@biit2.com";

	protected final String USER_PASSWORD = "asd123";

	private static final String SCREENSHOT_TYPE = ".jpeg";

	private final Login loginPage;
	private final FormManager formManager;
	private final BlockManager blockManager;
	private final Designer designer;
	private final FlowManager flowManager;
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

	public ButtonElement getFlowButton() {
		return $(ButtonElement.class).caption(FLOW_BUTTON_CAPTION).first();
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

	public ButtonElement getValidationButton(){
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
	
	public void goToFormManagerPage() {
		getFormsButton().click();
	}
	
	public void goToValidationPage() {
		getValidationButton().click();
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
		getLogoutButton().click();
		clickAcceptButtonIfExists();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
