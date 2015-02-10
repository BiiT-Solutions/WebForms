package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.WindowElement;

public class ImpactWindow extends VaadinGuiWindow {

	public static final String WINDOW_CAPTION = "";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";
	public static final String COMBOBOX_CAPTION = "Version";
	public static final String COMBOBOX_VALUE = "1";

	private void setComboBoxValue() {
		$(ComboBoxElement.class).caption(COMBOBOX_CAPTION).first().selectByText(COMBOBOX_VALUE);;
	}

	public void clickAcceptButton() {
		setComboBoxValue();
		$(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first().click();
	}

	public void closeWindow() {
		$$(WindowElement.class).caption(WINDOW_CAPTION).first().findElement(By.className("v-window-closebox")).click();
	}

}
