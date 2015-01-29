package com.biit.webforms.gui.test;

import com.biit.gui.tester.VaadinGuiTester;
import com.biit.webforms.gui.test.webpage.BlockManager;
import com.biit.webforms.gui.test.webpage.FormManager;
import com.biit.webforms.gui.test.webpage.Login;
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

	private final Login loginPage;
	private final FormManager formManager;
	private final BlockManager blockManager;

	private static final String FORMS_BUTTON_CAPTION = "Forms";
	private static final String BLOCKS_BUTTON_CAPTION = "Blocks";
	private static final String SETTINGS_BUTTON_CAPTION = "Settings";
	private static final String LOGOUT_BUTTON_CAPTION = "Log Out";
	private static final String ACCEPT_BUTTON_CAPTION = "Accept";

	public WebFormsTester() {
		super();
		loginPage = new Login();
		addWebpage(loginPage);
		formManager = new FormManager();
		addWebpage(formManager);
		blockManager = new BlockManager();
		addWebpage(blockManager);
	}

	public Login getLoginPage() {
		return loginPage;
	}

	public FormManager getFormManager() {
		return formManager;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

	public void login(String username, String password) {
		mainPage();
		loginPage.login(username, password);
	}

	public void loginRead1() {
		login(WEBFORMS_READ_BIIT1, USER_PASSWORD);
	}

	public void loginFormAdmin1() {
		login(WEBFORMS_FORM_ADMIN_BIIT1, USER_PASSWORD);
	}

	public void loginFormEdit1() {
		login(WEBFORMS_FORM_EDIT_BIIT1, USER_PASSWORD);
	}

	public void loginBlockEdit1() {
		login(WEBFORMS_BLOCK_EDIT_BIIT1, USER_PASSWORD);
	}

	public ButtonElement getBlocksButton() {
		return $(ButtonElement.class).caption(BLOCKS_BUTTON_CAPTION).first();
	}

	public ButtonElement getFormsButton() {
		return $(ButtonElement.class).caption(FORMS_BUTTON_CAPTION).first();
	}

	public ButtonElement getSettingsButton() {
		return $(ButtonElement.class).caption(SETTINGS_BUTTON_CAPTION).first();
	}

	public ButtonElement getLogoutButton() {
		showSettingsMenu();
		return $(ButtonElement.class).caption(LOGOUT_BUTTON_CAPTION).first();
	}

	public ButtonElement getAcceptButton() {
		if ($(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).exists()) {
			return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
		} else {
			return null;
		}
	}

	public void showSettingsMenu() {
		getSettingsButton().click();
	}

	public void logOut() {
		getLogoutButton().click();
		clickAcceptButtonIfExists();
	}

	public void clickAcceptButtonIfExists() {
		if (getAcceptButton() != null) {
			getAcceptButton().click();
		}
	}
	
	public void goToFormManager(){
		getFormsButton().click();
	}
	
	public void goToBlockManager(){
		getBlocksButton().click();
	}
}
