package com.biit.webforms.gui.tests.window;

import org.openqa.selenium.Keys;

import com.vaadin.testbench.By;

public class XmlTestsWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.WindowGenerateXml";
	public static final String STEPPER_CLASS = "gwt-TextBox";
	private static final String CLOSE_ELEMENT_NAME = "v-window-closebox";

	private void setStepperValue() {
		getWindow().findElement(By.className(STEPPER_CLASS)).sendKeys(Keys.ARROW_UP, Keys.ARROW_UP);
	}

	public void clickAcceptButton() {
		setStepperValue();
		clickAccept();
	}

	public void closeWindow() {
		getWindow().findElement(By.className(CLOSE_ELEMENT_NAME)).click();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}
