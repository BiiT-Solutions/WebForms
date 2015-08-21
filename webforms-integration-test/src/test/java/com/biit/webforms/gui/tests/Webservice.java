package com.biit.webforms.gui.tests;

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiTester;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.biit.webforms.gui.tests.window.SelectFormElementWindow;
import com.biit.webforms.gui.tests.window.WindowAddWebserviceCall;
import com.biit.webforms.gui.tests.window.WindowEditInputField;
import com.biit.webforms.gui.tests.window.WindowEditOutputField;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.PanelElement;
import com.vaadin.testbench.elements.TableElement;

public class Webservice extends VaadinGuiWebpage {

	private static final String ADD_BUTTON_CAPTION = "Add";

	private static final String SAVE_BUTTON_CAPTION = "Save";

	private static final String INPUT_FIELD_CAPTION = "Input fields";
	private static final String OUTPUT_FIELD_CAPTION = "Output fields";

	private final WindowAddWebserviceCall addWebserviceCall;
	private final WindowEditInputField editInputField;
	private final WindowEditOutputField editOutputField;
	private final SelectFormElementWindow selectFormElementWindow;

	public Webservice() {
		super();
		addWebserviceCall = new WindowAddWebserviceCall();
		addWindow(addWebserviceCall);
		editInputField = new WindowEditInputField();
		addWindow(editInputField);
		editOutputField = new WindowEditOutputField();
		addWindow(editOutputField);
		selectFormElementWindow = new SelectFormElementWindow();
		addWindow(selectFormElementWindow);
	}

	public WindowAddWebserviceCall getAddWebserviceCall() {
		return addWebserviceCall;
	}

	public ButtonElement getAddCallButton() {
		return getButtonElement(ADD_BUTTON_CAPTION);
	}

	public void addWebserviceCall(String name, int row) {
		getAddCallButton().click();
		getAddWebserviceCall().createNewWebserviceCall(name, row);
	}

	@Override
	public String getWebpageUrl() {
		return null;
	}

	public void save() {
		getSaveButton().click();
		getSaveButton().waitForVaadin();

		NotificationElement notification = $(NotificationElement.class).first();
		try {
			VaadinGuiTester.checkNotificationIsHumanized(notification);
			VaadinGuiTester.closeNotification(notification);
		} catch (NoSuchElementException e) {
			// Notification closed
		}
	}

	private TestBenchElement getSaveButton() {
		return getButtonElement(SAVE_BUTTON_CAPTION);
	}

	public void editInputField(int row) {
		$(TableElement.class).caption(INPUT_FIELD_CAPTION).first().getCell(row, 0).doubleClick();
	}

	public void editOutputField(int row) {
		$(TableElement.class).caption(OUTPUT_FIELD_CAPTION).first().getCell(row, 0).doubleClick();
	}

	public void editInputField(int port, int formElement) {
		editInputField(port);
		editInputField.editFormElement();
		selectFormElementWindow.selectFormElement(formElement);
		editInputField.clickAccept();
	}

	public void editOutputField(int port, int formElement, boolean editable) {
		editOutputField(port);
		editOutputField.editFormElement();
		selectFormElementWindow.selectFormElement(formElement);
		editOutputField.setEditableValue(editable);
		editOutputField.clickAccept();
	}

	public void selectTrigger(int row) {
		$(PanelElement.class).$(HorizontalLayoutElement.class).$$(HorizontalLayoutElement.class).first().click();
		selectFormElementWindow.selectFormElement(row);
	}
}
