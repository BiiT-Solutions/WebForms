package com.biit.webforms.gui.tests.window;

import org.openqa.selenium.Keys;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class XmlTestsWindow extends VaadinGuiWindow {

	public static final String DOWNLOAD_BUTTON_CAPTION = "Download file";
	public static final String FILE_GENERATION_SUCESSFUL_LABEL = "The file has been generated successfully!";
	public static final String WINDOW_CAPTION = "";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";
	public static final String STEPPER_CLASS = "gwt-TextBox";

	private void setStepperValue() {
		getDriver().findElement(By.className(STEPPER_CLASS)).sendKeys(Keys.ARROW_UP, Keys.ARROW_UP);
	}

	public void clickAcceptButton() {
		setStepperValue();
		$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first().click();
	}

	public void closeWindow() {
		$$(WindowElement.class).caption(WINDOW_CAPTION).first().findElement(By.className("v-window-closebox")).click();
	}

}
