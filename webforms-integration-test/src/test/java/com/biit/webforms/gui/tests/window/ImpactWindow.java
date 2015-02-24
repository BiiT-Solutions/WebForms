package com.biit.webforms.gui.tests.window;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ComboBoxElement;

public class ImpactWindow extends GenericAcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.webforms.gui.webpages.formmanager.WindowImpactAnalysis";
	private static final String COMBOBOX_CAPTION = "Version";
	private static final String COMBOBOX_VALUE = "1";
	private static final String CLOSE_ELEMENT_NAME = "v-window-closebox";

	private void setComboBoxValue() {
		getWindow().$(ComboBoxElement.class).caption(COMBOBOX_CAPTION).first().selectByText(COMBOBOX_VALUE);
	}

	public void clickAcceptButton() {
		setComboBoxValue();
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
