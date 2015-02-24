package com.biit.webforms.gui.tests.window;

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class FlowCleanedWindow extends VaadinGuiWindow {

	public static final String CLOSE_BUTTON_CAPTION = "Close";
	private static final String CLASS_NAME = "com.biit.webforms.gui.common.components.WindowTextArea";
	
	public ButtonElement getCloseButton() {
		if (getWindow() != null) {
			ElementQuery<ButtonElement> close = getWindow().$(ButtonElement.class).caption(CLOSE_BUTTON_CAPTION);
			if (close.exists()) {
				return close.first();
			}
		}
		return null;
	}
	
	public void clickCloseButton(){
		getCloseButton().click();
	}

	private String getWindowId() {
		return CLASS_NAME;
	}
	
	public WindowElement getWindow() {
		try {
			return $$(WindowElement.class).id(getWindowId());
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void waitToShow() {
		while (true) {
			try {
				if (getWindow() != null) {
					return;
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}
}
