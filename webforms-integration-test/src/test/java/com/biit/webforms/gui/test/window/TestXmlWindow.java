package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.WindowElement;

public class TestXmlWindow extends VaadinGuiWindow {

	public static final String DOWNLOAD_BUTTON_CAPTION = "Download file";
	public static final String FILE_GENERATION_SUCESSFUL_LABEL = "The file has been generated successfully!";
	public static final String WINDOW_CAPTION = "Download file.";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";
	
	public static final String STEPPER_VALUE = "3";
	
	private void setStepperValue() {
		$(TextFieldElement.class).first().setValue(STEPPER_VALUE);
	}

	public void clickAcceptButton(){
		setStepperValue();
		$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first().click();
	}
	

	public void closeWindow() {
		$$(WindowElement.class).caption(WINDOW_CAPTION).first().findElement(By.className("v-window-closebox")).click();
	}

}
