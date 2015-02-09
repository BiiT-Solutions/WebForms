package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class FlowCleanedWindow extends VaadinGuiWindow {

	public static final String WINDOW_CAPTION = "Flow cleaned";
	public static final String CLOSE_BUTTON_CAPTION = "Close";
	
	public boolean isWindowVisible(){
		return $$(WindowElement.class).caption(WINDOW_CAPTION).exists();
	}
	
	public void clickCloseButton(){
		$(ButtonElement.class).caption(CLOSE_BUTTON_CAPTION).first().click();
	}
}
