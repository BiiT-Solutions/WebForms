package com.biit.webforms.gui.test.webpage;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.window.NewForm;
import com.biit.webforms.gui.test.window.Proceed;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends VaadinGuiWebpage {

	private final static String SETTINGS_BUTTON_ID = "settingsButton";
	private final static String LOGOUT_BUTTON_ID = "logoutButton";

	private final NewForm newFormWindow;
	private final Proceed proceed;

	public FormManager() {
		super();
		newFormWindow = new NewForm();
		proceed = new Proceed();
		addWindow(newFormWindow);
		addWindow(proceed);
	}

	public void logOut() {
		$(ButtonElement.class).id(SETTINGS_BUTTON_ID).click();
		$(ButtonElement.class).id(LOGOUT_BUTTON_ID).click();
	}

	public ButtonElement getNewMenu() {
		return $(ButtonElement.class).caption("New").first();
	}

	public ButtonElement getNewForm() {
		getNewMenu().click();
		return $(ButtonElement.class).caption("Form").first();
	}

	/**
	 * If the element exists return the element otherwise return null.
	 * 
	 * @return
	 */
	public ButtonElement getRemoveForm() {
		if ($(ButtonElement.class).caption("Remove Form").exists()) {
			return $(ButtonElement.class).caption("Remove Form").first();
		}
		return null;
	}

	private void openNewForm() {
		getNewForm().click();
	}

	public void createNewForm(String formName) {
		openNewForm();
		newFormWindow.createNewForm(formName);
	}

	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}

	@Override
	public String getWebpageUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteForm(int row) {
		getFormTable().getRow(row).click();
		getRemoveForm().click();
		proceed.clickAccept();
	}

}
