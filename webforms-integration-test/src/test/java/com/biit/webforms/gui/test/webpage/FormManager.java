package com.biit.webforms.gui.test.webpage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.exceptions.OrganizationNotEditableException;
import com.biit.webforms.gui.test.window.NewFormWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends VaadinGuiWebpage {

	private static final String NEW_BUTTON_CAPTION = "New";
	private static final String FORM_BUTTON_CAPTION = "Form";
	private static final String REMOVE_FORM_BUTTON_CAPTION = "Remove Form";
	private static final String EXPORT_BUTTON_CAPTION = "Export";

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

	public void createNewFormWithOrganization(String formName, String organizationName)
			throws OrganizationNotEditableException {
		openNewFormWindow();
		getNewFormWindow().createNewFormWithOrganization(formName, organizationName);
	}

	public void deleteForm(int row) {
		// To avoid errors, first we select other element of the table
		getFormTable().getCell(0, 0).click();
		getFormTable().getCell(row, 0).click();
		Assert.assertNotNull(getRemoveForm());
		getRemoveForm().click();
		clickAcceptButtonIfExists();
	}

	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}

	public ComboBoxElement getFormStatusComboBox() {
		return $(ComboBoxElement.class).first();
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

	@Override
	public String getWebpageUrl() {
		return null;
	}

	private void openNewFormWindow() {
		getNewFormButton().click();
	}

	/**
	 * Workaround to close the popover.<br>
	 * When the popover is displayed only the element inside the popover can be
	 * selected.<br>
	 * To close it, we have focus it and send the close key defined.
	 */
	public void closeNewPopover() {
		getNewFormButton().focus();
		Actions builder = new Actions(getDriver());
		builder.sendKeys(Keys.ESCAPE).perform();
	}
}
