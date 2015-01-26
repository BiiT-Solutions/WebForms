package com.biit.webforms.gui.test;

import com.biit.gui.tester.VaadinGuiTester;
import com.biit.webforms.gui.test.webpage.FormManager;
import com.biit.webforms.gui.test.webpage.Login;

public class WebFormsTester extends VaadinGuiTester {

	protected final String WEBFORMS_READ_BIIT1 = "webforms_read@biit1.com";
	protected final String WEBFORMS_FORM_EDIT_BIIT1 = "webforms_form_edit@biit1.com";
	protected final String WEBFORMS_BLOCK_EDIT_BIIT1 = "webforms_block_edit@biit.com";
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

	public WebFormsTester() {
		super();
		loginPage = new Login();
		addWebpage(loginPage);
		formManager = new FormManager();
		addWebpage(formManager);
	}

	public Login getLoginPage() {
		return loginPage;
	}

	public FormManager getFormManager() {
		return formManager;
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

	public void loginBlockdit1() {
		login(WEBFORMS_BLOCK_EDIT_BIIT1, USER_PASSWORD);
	}
}
