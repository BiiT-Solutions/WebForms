package com.biit.webforms.gui.test.webpage;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.window.NewFormWindow;
import com.biit.webforms.gui.test.window.Proceed;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends VaadinGuiWebpage {

	private final static String LOGOUT_BUTTON_ID = "logoutButton";
	private final static String SETTINGS_BUTTON_ID = "settingsButton";
	private final static String BLOCKS_BUTTON_CAPTION = "Blocks";
	private final static String NEW_BUTTON_CAPTION = "New";
	private final static String FORM_BUTTON_CAPTION = "Form";
	private final static String REMOVE_FORM_BUTTON_CAPTION = "Remove Form";

	private final NewFormWindow newFormWindow;
	private final Proceed proceed;

	public FormManager() {
		super();
		newFormWindow = new NewFormWindow();
		proceed = new Proceed();
		addWindow(newFormWindow);
		addWindow(proceed);
	}

	public void createNewForm(String formName) {
		openNewFormWindow();
		newFormWindow.createNewForm(formName);
	}
	
	public void deleteForm(int row) {
		getFormTable().getCell(row, 0).click();
		getRemoveForm().click();
		proceed.clickAccept();
	}
	
	public ButtonElement getBlocks() {
		return $(ButtonElement.class).caption(BLOCKS_BUTTON_CAPTION).first();
	}

	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}

	public ButtonElement getLogOut() {
		getSettingsMenu().click();
		return $(ButtonElement.class).id(LOGOUT_BUTTON_ID);
	}

	public ButtonElement getNewForm() {
		getNewMenu().click();
		return $(ButtonElement.class).caption(FORM_BUTTON_CAPTION).first();
	}

	public ButtonElement getNewMenu() {
		return $(ButtonElement.class).caption(NEW_BUTTON_CAPTION).first();
	}

	/**
	 * If the element exists return the element otherwise return null.
	 * 
	 * @return
	 */
	public ButtonElement getRemoveForm() {
		if ($(ButtonElement.class).caption(REMOVE_FORM_BUTTON_CAPTION).exists()) {
			return $(ButtonElement.class).caption(REMOVE_FORM_BUTTON_CAPTION).first();
		}
		return null;
	}

	public ButtonElement getSettingsMenu() {
		return $(ButtonElement.class).id(SETTINGS_BUTTON_ID);
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public void logOut() {
		getLogOut().click();
	}
	
	public void openBlocks() {
		getBlocks().click();
	}

	private void openNewFormWindow() {
		getNewForm().click();
	}

}
