package com.biit.webforms.gui.test.webpage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.test.exceptions.OrganizationNotEditableException;
import com.biit.webforms.gui.test.window.DownloadWindow;
import com.biit.webforms.gui.test.window.NewFormWindow;
import com.biit.webforms.gui.test.window.TestXmlWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends VaadinGuiWebpage {

	private static final String NEW_BUTTON_CAPTION = "New";
	private static final String FORM_BUTTON_CAPTION = "Form";
	private static final String REMOVE_FORM_BUTTON_CAPTION = "Remove Form";
	private static final String EXPORT_BUTTON_CAPTION = "Export";
	private static final String EXPORT_FORM_BUTTON_CAPTION = "Form";
	private static final String EXPORT_FLOW_BUTTON_CAPTION = "Flow";
	private static final String EXPORT_XSD_BUTTON_CAPTION = "XSD";
	private static final String EXPORT_TEST_XMLS_BUTTON_CAPTION = "Test Xml's";
	private static final String EXPORT_JSON_BUTTON_CAPTION = "Json";
	private static final Integer RIGHT_SCROLL_PIXELS = 500;
	
	private static final String FLOW_BUTTON_ID = "exportFlowButton";

	private final NewFormWindow newFormWindow;
	private final DownloadWindow downloadWindow;
	private final TestXmlWindow testXmlWindow;

	public FormManager() {
		super();
		newFormWindow = new NewFormWindow();
		addWindow(newFormWindow);
		downloadWindow = new DownloadWindow();
		addWindow(downloadWindow);
		testXmlWindow = new TestXmlWindow();
		addWindow(testXmlWindow);
	}

	public void clickExportButton() {
		getExportButton().waitForVaadin();
		getExportButton().click();
	}

	public void clickExportFlowButton() {
		getExportFlowButton().click();
	}

	public void clickExportFormButton() {
		getExportFormButton().click();
	}

	public void clickExportJsonButton() {
		getExportJsonButton().click();
	}

	public void clickExportTestXmlsButton() {
		getExportTestXmlsButton().click();
	}

	public void clickExportXsdButton() {
		getExportXsdButton().click();
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

	public void createNewForm(String formName) {
		if(isFormAlreadyCreated(formName)){
			deleteForm(1);
		}
		openNewFormWindow();
		getNewFormWindow().createNewForm(formName);
	}
	
	public boolean isFormAlreadyCreated(String formName) {
		try {
			getFormTable().getCell(1, 0).getText().equals(formName);
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
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

	@Override
	public ButtonElement getButtonElement(String buttonCaption) {
		if (!existsButton(buttonCaption)) {
			scrollRightUpperButtonMenu();
		}
		return super.getButtonElement(buttonCaption);
	}

	public DownloadWindow getDownloadWindow() {
		return downloadWindow;
	}

	public ButtonElement getExportButton() {
		return getButtonElement(EXPORT_BUTTON_CAPTION);
	}

	public ButtonElement getExportFlowButton() {
		return $(ButtonElement.class).id(FLOW_BUTTON_ID);
	}

	public ButtonElement getExportFormButton() {
		return getButtonElement(EXPORT_FORM_BUTTON_CAPTION);
	}

	public ButtonElement getExportJsonButton() {
		return getButtonElement(EXPORT_JSON_BUTTON_CAPTION);
	}

	public ButtonElement getExportTestXmlsButton() {
		return getButtonElement(EXPORT_TEST_XMLS_BUTTON_CAPTION);
	}

	public ButtonElement getExportXsdButton() {
		return getButtonElement(EXPORT_XSD_BUTTON_CAPTION);
	}

	public ComboBoxElement getFormStatusComboBox() {
		return $(ComboBoxElement.class).first();
	}

	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}

	public ButtonElement getNewButton() {
		return $(ButtonElement.class).caption(NEW_BUTTON_CAPTION).first();
	}

	public ButtonElement getNewFormButton() {
		return $(ButtonElement.class).caption(FORM_BUTTON_CAPTION).first();
	}
	
	public void clickNewButton(){
		getNewButton().click();
	}
	
	public void clickNewFormButton(){
		getNewButton().click();
	}

	public NewFormWindow getNewFormWindow() {
		return newFormWindow;
	}

	/**
	 * If the element exists return the element otherwise return null.
	 * 
	 * @return
	 */
	public ButtonElement getRemoveForm() {
		return getButtonElement(REMOVE_FORM_BUTTON_CAPTION);
	}

	public TestXmlWindow getTestXmlWindow() {
		return testXmlWindow;
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	private void openNewFormWindow() {
		getNewButton().click();
		getNewFormButton().click();
	}

	private void scrollRightUpperButtonMenu() {
		$(HorizontalLayoutElement.class).$$(HorizontalLayoutElement.class).first().scrollLeft(RIGHT_SCROLL_PIXELS);
	}

}
