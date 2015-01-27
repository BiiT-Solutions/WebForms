package com.biit.webforms.gui.test.webpage;

import org.testng.Assert;

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
	private final static String EXPORT_BUTTON_CAPTION = "Export";
	private final static String LOG_OUT_BUTTON_CAPTION = "Log Out";
	private final static String ACCEPT_BUTTON_CAPTION = "Accept";

	private final NewFormWindow newFormWindow;

	public FormManager() {
		super();
		newFormWindow = new NewFormWindow();
		addWindow(newFormWindow);
	}

	public NewFormWindow getNewFormWindow() {
		return newFormWindow;
	}

	public void createNewForm(String formName) {
		openNewFormWindow();
		getNewFormWindow().createNewForm(formName);
	}

	public void deleteForm(int row) {
		// To avoid errors, first we select other element of the table
		getFormTable().getCell(0, 0).click();
		getFormTable().getCell(row, 0).click();
		Assert.assertNotNull(getRemoveForm());
		getRemoveForm().click();
		getAcceptButton().click();
	}

	public ButtonElement getBlocksButton() {
		return $(ButtonElement.class).caption(BLOCKS_BUTTON_CAPTION).first();
	}

	public ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}

	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}

	public ButtonElement getLogOutButton() {
		getSettingsMenu().click();
		if ($(ButtonElement.class).caption(LOG_OUT_BUTTON_CAPTION).exists()) {
			return $(ButtonElement.class).id(LOGOUT_BUTTON_ID);
		} else {
			// Click again to force the display of the submenu
			getSettingsMenu().click();
			if ($(ButtonElement.class).caption(LOG_OUT_BUTTON_CAPTION).exists()) {
				return $(ButtonElement.class).id(LOGOUT_BUTTON_ID);
			} else {
				return null;
			}
		}
	}

	public ButtonElement getNewFormButton() {
		getNewButton().click();
		return $(ButtonElement.class).caption(FORM_BUTTON_CAPTION).first();
	}

	public ButtonElement getNewButton() {
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

	public ButtonElement getExportButton() {
		if ($(ButtonElement.class).caption(EXPORT_BUTTON_CAPTION).exists()) {
			return $(ButtonElement.class).caption(EXPORT_BUTTON_CAPTION).first();
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
		getLogOutButton().click();
		if ($(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).exists()) {
			$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first().click();
		}
	}

	public void openBlocks() {
		getBlocksButton().click();
	}

	private void openNewFormWindow() {
		getNewFormButton().click();
	}
}
